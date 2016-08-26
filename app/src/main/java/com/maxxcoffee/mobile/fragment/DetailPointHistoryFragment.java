package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.PointHistoryAdapter;
import com.maxxcoffee.mobile.database.controller.HistoryController;
import com.maxxcoffee.mobile.database.entity.HistoryEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class DetailPointHistoryFragment extends Fragment {

    @Bind(R.id.recycleview)
    ListView recyclerView;
    @Bind(R.id.city_layout)
    RelativeLayout cityLayout;
    @Bind(R.id.empty)
    TextView empty;

    private FormActivity activity;
    private List<HistoryEntity> data;
    private PointHistoryAdapter adapter;
    private HistoryController historyController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();

        historyController = new HistoryController(activity);
        data = new ArrayList<>();
        adapter = new PointHistoryAdapter(activity, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        fetchingData();

        cityLayout.setVisibility(View.GONE);
        return view;
    }

    private void fetchingData() {
        data.clear();
        List<HistoryEntity> histories = historyController.getHistoryByType("pointRedemption", "topupPoint");

        empty.setVisibility(histories.size() == 0 ? View.VISIBLE : View.GONE);
        for (HistoryEntity history : histories) {
            data.add(history);
        }
        adapter.notifyDataSetInvalidated();
    }
}
