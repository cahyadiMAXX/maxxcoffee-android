package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.StoreAdapter;
import com.maxxcoffee.mobile.database.entity.StoreEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.response.StoreItemResponseModel;
import com.maxxcoffee.mobile.task.NearestStoreTask;
import com.maxxcoffee.mobile.util.GpsTracker;
import com.maxxcoffee.mobile.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */

@SuppressWarnings("MissingPermission")
public class NearMeFragment extends Fragment implements LocationListener {

    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Bind(R.id.recycleview)
    ListView recyclerView;
    @Bind(R.id.city_layout)
    RelativeLayout searchLayout;
    @Bind(R.id.empty)
    TextView empty;

    private MainActivity activity;
    private List<StoreEntity> data;
    private StoreAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        data = new ArrayList<>();
        adapter = new StoreAdapter(activity, data, true) {
            @Override
            protected void onCall(String phone) {
                if (!phone.equals("") && !phone.equals("TBA") && !phone.equals("N/A")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Phone number not valid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onOpenMap(String lat, String lng, String place) {
                String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + place + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        searchLayout.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

        //fetchingData();
        checkBeforeFetchData();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                //fetchingData();
                checkBeforeFetchData();
            }
        });
        swipe.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    //fetchingData();
                    checkBeforeFetchData();
                }
            }
        });
        return view;
    }

    private void checkBeforeFetchData(){
        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            //ambil lokal dulu

            //adapter.notifyDataSetChanged();
        }else{
            fetchingData();
        }
    }

    @OnItemClick(R.id.recycleview)
    public void onStoreClick(int position) {
        StoreEntity model = data.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("title", model.getName());
        bundle.putString("address", model.getAddress());
        bundle.putString("contact", model.getPhone());
        bundle.putString("open", model.getOpen());
        bundle.putString("close", model.getClose());
        bundle.putString("images", model.getImage());
        bundle.putString("feature-icon", model.getFeature_icon());
        bundle.putString("feature", model.getFeature());
        bundle.putString("jarak", model.getJarak());
        bundle.putString("lat", model.getLatitude());
        bundle.putString("lng", model.getLongitude());

        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.STORE_DETAIL);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void fetchingData() {

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        GpsTracker gpsTracker = new GpsTracker(activity);

        String lat = String.valueOf(gpsTracker.getLatitude());
        String ltg = String.valueOf(gpsTracker.getLongitude());

        NearestStoreTask task = new NearestStoreTask(activity) {
            @Override
            public void onSuccess(List<StoreItemResponseModel> responseModel) {

                if (responseModel.size() > 0)
                    data.clear();

                empty.setText(responseModel.size() == 0 ? "Data not found" : "");
                empty.setVisibility(responseModel.size() == 0 ? View.VISIBLE : View.GONE);
                for (StoreItemResponseModel storeItem : responseModel) {
                    String jsonFeature = new Gson().toJson(storeItem.getFeature());
                    String jsonFeatureIcon = new Gson().toJson(storeItem.getIcon_feature());
                    String jsonFoto = new Gson().toJson(storeItem.getFoto());

                    StoreEntity store = new StoreEntity();
                    store.setId(storeItem.getId_store());
                    store.setName(storeItem.getNama_store().trim());
                    store.setAddress(storeItem.getAlamat_store());
                    store.setCity(storeItem.getKota_store().trim());
                    store.setProvince(storeItem.getProvinsi_store().trim());
                    store.setIsland(storeItem.getPulau());
                    store.setZipcode(storeItem.getKodepos_store());
                    store.setLatitude(storeItem.getLatitude());
                    store.setLongitude(storeItem.getLongitude());
                    store.setOpen(storeItem.getJam_buka());
                    store.setClose(storeItem.getJam_tutup());
                    store.setPhone(storeItem.getPhone_store());
                    store.setFeature(jsonFeature);
                    store.setFeature_icon(jsonFeatureIcon);
                    store.setImage(jsonFoto);
                    store.setJarak(storeItem.getJarak_km());

                    data.add(store);
                    adapter.notifyDataSetInvalidated();
                }
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(lat, ltg);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
