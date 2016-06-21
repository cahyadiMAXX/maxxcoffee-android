package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.StoreAdapter;
import com.maxxcoffee.mobile.database.controller.StoreGroupController;
import com.maxxcoffee.mobile.database.controller.StoreItemController;
import com.maxxcoffee.mobile.database.entity.StoreItemEntity;
import com.maxxcoffee.mobile.fragment.dialog.StoreDialog;
import com.maxxcoffee.mobile.model.StoreModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class OurStoreFragment extends Fragment {

    @Bind(R.id.recycleview)
    ListView recyclerView;

    private MainActivity activity;
    private List<StoreModel> data;
    private StoreAdapter adapter;
    private StoreGroupController storeGroupController;
    private StoreItemController storeItemController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        storeGroupController = new StoreGroupController(activity);
        storeItemController = new StoreItemController(activity);
        data = new ArrayList<>();
        adapter = new StoreAdapter(activity, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        fetchingData();
        return view;
    }

    @OnItemClick(R.id.recycleview)
    public void onStoreClick(int position) {
        StoreModel model = data.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("title", model.getName());
        bundle.putString("address", model.getAddress());
        bundle.putString("contact", model.getContact());
        bundle.putString("open", model.getOpen());

        StoreDialog dialog = new StoreDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), null);
    }

    private void fetchingData() {
        List<StoreItemEntity> stores = storeItemController.getStoreItems();
        for (StoreItemEntity store : stores) {
            StoreModel child = new StoreModel();
            child.setId(store.getId());
            child.setStoreId(store.getStoreId());
            child.setName(store.getName());
            child.setAddress(store.getAddress());
            child.setContact(store.getContact());
            child.setOpen(store.getOpen());
            data.add(child);
        }
        adapter.notifyDataSetChanged();
    }
}
