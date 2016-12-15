package com.maxxcoffee.mobile.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.StoreAdapter;
import com.maxxcoffee.mobile.database.controller.ProvinceController;
import com.maxxcoffee.mobile.database.controller.StoreController;
import com.maxxcoffee.mobile.database.entity.StoreEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.ProvinceListDialog;
import com.maxxcoffee.mobile.model.response.StoreItemResponseModel;
import com.maxxcoffee.mobile.task.StoreTask;
import com.maxxcoffee.mobile.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class OurStoreFragment extends Fragment {

    private static String[] PERMISSIONS_CALL_PHONE = {Manifest.permission.CALL_PHONE};

    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Bind(R.id.recycleview)
    ListView recyclerView;
    @Bind(R.id.search)
    TextView search;
    @Bind(R.id.empty)
    TextView empty;

    private MainActivity activity;
    private List<StoreEntity> data;
    private List<String> dataProvince;
    private StoreAdapter adapter;
    private StoreController controller;
    private ProvinceController provinceController;

    public OurStoreFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        controller = new StoreController(activity);
        provinceController = new ProvinceController(activity);
        data = new ArrayList<>();
        dataProvince = new ArrayList<>();
        adapter = new StoreAdapter(activity, data) {
            @Override
            protected void onCall(String phone) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {

                        ActivityCompat.requestPermissions(activity, PERMISSIONS_CALL_PHONE, 2);
                    } else {
                        ActivityCompat.requestPermissions(activity, PERMISSIONS_CALL_PHONE, 2);
                    }
                } else {
                    call(phone);
                }

            }

            @Override
            protected void onOpenMap(String lat, String lng, String place) {
//                String uri = String.format(Locale.ENGLISH, "geo:%s,%s?q=%s,%s($s)", lat, lng, lat, lng);
                String uri = "geo:" + lat + "," + lng + "?q=" + lat + "," + lng + "(" + place + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getActivity().startActivity(intent);
            }
        };
    }

    private void call(String phone) {
        if (!phone.equals("") && !phone.equals("TBA") && !phone.equals("N/A")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Phone number not valid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        checkBeforeFetchData();

        //swipe.setEnabled(false);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                //fetchingData();
                checkBeforeFetchData();
            }
        });

        search.requestFocus();
        return view;
    }

    private void checkBeforeFetchData(){
        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            adapter.notifyDataSetInvalidated();
            getLocalProvince();
        }else{
            getLocalProvince();
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
        bundle.putString("jarak", "-999");
        bundle.putString("lat", model.getLatitude());
        bundle.putString("lng", model.getLongitude());

        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.STORE_DETAIL);
        intent.putExtras(bundle);
        startActivity(intent);

//        StoreDialog dialog = new StoreDialog();
//        dialog.setArguments(bundle);
//        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.city_layout)
    public void onCityClick() {
        try{
            Collections.sort(dataProvince);
            dataProvince.set(0, "ALL");
            String jsonProvince = new Gson().toJson(dataProvince);

            Bundle bundle = new Bundle();
            bundle.putString("provinces", jsonProvince);

            ProvinceListDialog provinceDialog = new ProvinceListDialog() {
                @Override
                public void onSelectedItem(String item) {
                    search.setText(item);
                    getLocalStore(item);
                    dismiss();
                }
            };
            provinceDialog.setArguments(bundle);
            provinceDialog.show(getFragmentManager(), null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fetchingData() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        StoreTask task = new StoreTask(activity) {
            @Override
            public void onSuccess(List<StoreItemResponseModel> responseModel) {

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

                    controller.insert(store);
                }
                getLocalProvince();
                loading.dismiss();
                //progress.dismissAllowingStateLoss();

            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    public void getLocalStore(String province) {
        List<StoreEntity> stores = new ArrayList<>();
        /*LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        if (province.equalsIgnoreCase("all")) {
            stores = controller.getStores();
        } else {
            stores = controller.getStores(province);
        }

        if (stores.size() > 0)
            data.clear();

        data.addAll(stores);
        adapter.notifyDataSetInvalidated();
        //progress.dismissAllowingStateLoss();
        loading.dismiss();
    }

    public void getLocalProvince() {
        String firstProvince = "DKI Jakarta";
        List<String> tempProvince = new ArrayList<>();
        List<StoreEntity> data = controller.getStoreProvices();
        if (data.size() > 0)
            dataProvince.clear();

        for (StoreEntity store : data) {
            tempProvince.add(store.getProvince().trim());
        }

        dataProvince = removeDuplicates(tempProvince);

        search.setText(firstProvince);
        getLocalStore(firstProvince);
    }

    private ArrayList<String> removeDuplicates(List<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }
}
