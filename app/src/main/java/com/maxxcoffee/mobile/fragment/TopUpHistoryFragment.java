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
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.TransferHistoryAdapter;
import com.maxxcoffee.mobile.database.controller.HistoryController;
import com.maxxcoffee.mobile.database.entity.HistoryEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class TopUpHistoryFragment extends Fragment {

    @Bind(R.id.recycleview)
    ListView recyclerView;
    @Bind(R.id.city_layout)
    RelativeLayout cityLayout;
    @Bind(R.id.empty)
    TextView empty;

    private MainActivity activity;
    private List<HistoryEntity> data;
    private TransferHistoryAdapter adapter;
    private HistoryController historyController;

    public TopUpHistoryFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        historyController = new HistoryController(activity);
        data = new ArrayList<>();
        adapter = new TransferHistoryAdapter(activity, data, false, false);
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
        List<HistoryEntity> histories = historyController.getHistoryByType("topup");

        empty.setVisibility(histories.size() == 0 ? View.VISIBLE : View.GONE);
        empty.setText(histories.size() == 0 ? "Data not found" : "");
        for (HistoryEntity history : histories) {
            data.add(history);
        }
        adapter.notifyDataSetInvalidated();
    }
}
