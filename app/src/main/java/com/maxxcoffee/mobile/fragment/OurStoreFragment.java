package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.StoreAdapter;
import com.maxxcoffee.mobile.database.controller.StoreController;
import com.maxxcoffee.mobile.database.entity.StoreEntity;
import com.maxxcoffee.mobile.fragment.dialog.ListDialog;
import com.maxxcoffee.mobile.fragment.dialog.StoreDialog;
import com.maxxcoffee.mobile.model.response.StoreItemResponseModel;
import com.maxxcoffee.mobile.task.StoreTask;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class OurStoreFragment extends Fragment {

    @Bind(R.id.recycleview)
    ListView recyclerView;
    @Bind(R.id.search)
    TextView search;

    private MainActivity activity;
    private List<StoreEntity> data;
    private List<String> dataProvince;
    private StoreAdapter adapter;
    private StoreController controller;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        controller = new StoreController(activity);
        data = new ArrayList<>();
        dataProvince = new ArrayList<>();
        adapter = new StoreAdapter(activity, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        getLocalProvince();
        fetchingData();

        search.requestFocus();
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

    @OnClick(R.id.city_layout)
    public void onCityClick() {
        String jsonProvince = new Gson().toJson(dataProvince);

        Bundle bundle = new Bundle();
        bundle.putString("provinces", jsonProvince);

        ListDialog provinceDialog = new ListDialog() {
            @Override
            public void onSelectedItem(String item) {
                search.setText(item);
                getLocalStore(item);
                dismiss();
            }
        };
        provinceDialog.setArguments(bundle);
        provinceDialog.show(getFragmentManager(), null);
    }

    private void fetchingData() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        StoreTask task = new StoreTask(activity) {
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

                    controller.insert(store);
                }
                getLocalProvince();

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
        task.execute();
    }

    public void getLocalStore(String province) {
        TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        List<StoreEntity> stores = controller.getStores(province);
        if (stores.size() > 0)
            data.clear();

        data.addAll(stores);
        adapter.notifyDataSetInvalidated();

        if (progress.isShowing())
            progress.dismiss();
    }

    public void getLocalProvince() {
        String firstProvince = "";
        List<StoreEntity> data = controller.getStoreProvices();
        if (data.size() > 0)
            dataProvince.clear();

        for (StoreEntity store : data) {
            if (firstProvince.equalsIgnoreCase(""))
                firstProvince = store.getProvince();
            dataProvince.add(store.getProvince());
        }

        search.setText(firstProvince);
        getLocalStore(firstProvince);
    }
}
