package com.maxxcoffee.mobile.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.StoreAdapter;
import com.maxxcoffee.mobile.database.entity.StoreEntity;
import com.maxxcoffee.mobile.fragment.dialog.StoreDialog;
import com.maxxcoffee.mobile.model.response.StoreItemResponseModel;
import com.maxxcoffee.mobile.task.NearestStoreTask;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class NearMeFragment extends Fragment implements LocationListener {

    @Bind(R.id.recycleview)
    ListView recyclerView;
    @Bind(R.id.city_layout)
    RelativeLayout searchLayout;

    private MainActivity activity;
    private List<StoreEntity> data;
    private StoreAdapter adapter;
    private LocationManager locationManager;
    private Criteria criteria;
    private Location myLocation;
    private LatLng myPosition;

    private boolean myLocationFetched = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        data = new ArrayList<>();
        adapter = new StoreAdapter(activity, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        searchLayout.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

        getCurrentLocation();
        fetchingData();
        return view;
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
        bundle.putString("image", model.getImage());
        bundle.putString("feature-icon", model.getFeature_icon());
        bundle.putString("feature", model.getFeature());


        StoreDialog dialog = new StoreDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), null);
    }


    private void fetchingData() {

        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        String lat = String.valueOf(myPosition.latitude);
        String ltg = String.valueOf(myPosition.longitude);

        NearestStoreTask task = new NearestStoreTask(activity) {
            @Override
            public void onSuccess(List<StoreItemResponseModel> responseModel) {
                for (StoreItemResponseModel storeItem : responseModel) {
                    String jsonFeature = new Gson().toJson(storeItem.getFeature());
                    String jsonFeatureIcon = new Gson().toJson(storeItem.getIcon_feature());
                    String jsonFoto = new Gson().toJson(storeItem.getFoto());

                    StoreEntity store = new StoreEntity();
                    store.setId(storeItem.getId_store());
                    store.setName(storeItem.getNama_store());
                    store.setAddress(storeItem.getAlamat_store());
                    store.setCity(storeItem.getKota_store());
                    store.setProvince(storeItem.getProvinsi_store());
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

                    data.add(store);
                    adapter.notifyDataSetInvalidated();
                }

                if (progress.isShowing())
                    progress.dismiss();
            }

            @Override
            public void onFailed() {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity, "Failed to fetching data.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(lat, ltg);

//        List<StoreItemEntity> stores = storeItemController.getStoreItems();
//        for (StoreItemEntity store : stores) {
//            StoreModel child = new StoreModel();
//            child.setId(store.getId());
//            child.setStoreId(store.getStoreId());
//            child.setName(store.getName());
//            child.setAddress(store.getAddress());
//            child.setContact(store.getContact());
//            child.setOpen(store.getOpen());
//            data.add(child);
//        }
//        adapter.notifyDataSetChanged();
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;

        locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(activity, "Failed retrieve data. Turn on your GPS First.", Toast.LENGTH_SHORT).show();
        } else {

            if (myLocationFetched) return;

            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
                Log.d("Network", "Network Enabled");
                if (locationManager != null) {
                    myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (myLocation != null) {
                        myLocationFetched = true;
                        double latitude = myLocation.getLatitude();
                        double longitude = myLocation.getLongitude();
                        myPosition = new LatLng(latitude, longitude);
                    }
                }
            }

            if (isGPSEnabled) {
                if (myLocation == null) {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
                    if (locationManager != null) {
                        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (myLocation != null) {
                            myLocationFetched = true;
                            double latitude = myLocation.getLatitude();
                            double longitude = myLocation.getLongitude();
                            myPosition = new LatLng(latitude, longitude);
                        }
                    }
                }
            }
        }
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
