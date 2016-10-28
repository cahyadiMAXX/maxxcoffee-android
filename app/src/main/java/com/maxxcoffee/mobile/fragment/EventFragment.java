package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.EventAdapter;
import com.maxxcoffee.mobile.database.controller.EventController;
import com.maxxcoffee.mobile.database.entity.EventEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.response.EventItemResponseModel;
import com.maxxcoffee.mobile.task.EventTask;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class EventFragment extends Fragment {

    @Bind(R.id.recycleview)
    RecyclerView recyclerView;
    @Bind(R.id.empty_card)
    TextView emptyCard;

    private MainActivity activity;
    private List<EventEntity> data;
    private EventAdapter adapter;
    private EventController eventController;
    private CustomLinearLayoutManager layoutManager;

    public EventFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        eventController = new EventController(activity);
        data = new ArrayList<>();
        adapter = new EventAdapter(activity, data) {
            @Override
            public void onCardClick(EventEntity model) {
                Bundle bundle = new Bundle();
                bundle.putInt("event-id", model.getId_event());

                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.EVENT_DETAIL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Event");

        layoutManager = new CustomLinearLayoutManager(activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        //getLocalPromo();
        //fetchingData();
        emptyCard.setVisibility(View.GONE);
        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            getLocalPromo();
        }

        return view;
    }

    private void fetchingData() {
        emptyCard.setVisibility(View.GONE);
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        EventTask task = new EventTask(activity) {
            @Override
            public void onSuccess(List<EventItemResponseModel> responseModel) {
                eventController.clearTable();

                for (int i = 0 ; i < responseModel.size(); i++) {
                    EventItemResponseModel event = responseModel.get(i);
                    EventEntity entity = new EventEntity();
                    //entity.setId_event(event.getId_event());
                    entity.setId_event(i+1);
                    entity.setNama_event(event.getNama_event());
                    entity.setNama_lokasi(event.getNama_lokasi());
                    entity.setAlamat_lokasi(event.getAlamat_lokasi());
                    entity.setNo_telp(event.getNo_telp());
                    entity.setLatitude(event.getLatitude());
                    entity.setLongitude(event.getLongitude());
                    entity.setTanggal_start(event.getTanggal_start());
                    entity.setTanggal_end(event.getTanggal_end());
                    entity.setWaktu_start(event.getWaktu_start());
                    entity.setWaktu_end(event.getWaktu_end());
                    entity.setDeskripsi(event.getDeskripsi());
                    entity.setGambar(event.getGambar());
                    entity.setLs_gambar(event.getLs_gambar());
                    eventController.insert(entity);
                }

                getLocalPromo();
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onEmpty() {
                emptyCard.setText("We do not have any event");
                emptyCard.setVisibility(View.VISIBLE);
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void getLocalPromo() {
        List<EventEntity> events = eventController.getEvents();
        emptyCard.setText(events.size() > 0 ? "" : "We do not have any event");
        emptyCard.setVisibility(events.size() > 0 ? View.GONE : View.VISIBLE);

        if (events.size() > 0)
            data.clear();

        data.addAll(events);
        adapter.notifyDataSetChanged();
    }
}
