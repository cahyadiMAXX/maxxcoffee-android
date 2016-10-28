package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.HistoryItemAdapter;
import com.maxxcoffee.mobile.model.HistoryModel;
import com.maxxcoffee.mobile.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Rio Swarawan on 5/23/2016.
 */
public class TransactionFragment extends Fragment {

    @Bind(R.id.recycleview)
    ListView recyclerView;

    private MainActivity activity;
    private List<HistoryModel> data;
    private HistoryItemAdapter adapter;

    public TransactionFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        data = new ArrayList<>();
        adapter = new HistoryItemAdapter(activity, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @OnItemClick(R.id.recycleview)
    public void onStoreClick(int position) {
    }

    private void fetchingData() {

    }
}
