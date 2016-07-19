package com.maxxcoffee.mobile.fragment;

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
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.EventAdapter;
import com.maxxcoffee.mobile.adapter.PromoAdapter;
import com.maxxcoffee.mobile.database.controller.EventController;
import com.maxxcoffee.mobile.database.controller.PromoController;
import com.maxxcoffee.mobile.database.entity.EventEntity;
import com.maxxcoffee.mobile.database.entity.PromoEntity;
import com.maxxcoffee.mobile.fragment.dialog.EventDialog;
import com.maxxcoffee.mobile.fragment.dialog.PromoDialog;
import com.maxxcoffee.mobile.model.response.EventItemResponseModel;
import com.maxxcoffee.mobile.model.response.PromoItemResponseModel;
import com.maxxcoffee.mobile.task.EventTask;
import com.maxxcoffee.mobile.task.PromoTask;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;
import com.maxxcoffee.mobile.widget.TBaseProgress;

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
                bundle.putString("title", model.getEvent_name());
                bundle.putString("date", model.getDescription());
                bundle.putString("store", model.getStore_name());
                bundle.putString("location", model.getDescription());
                bundle.putString("image", model.getImage());

                EventDialog dialog = new EventDialog();
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), null);
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

        getLocalPromo();
        fetchingData();

        return view;
    }

    private void fetchingData() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        EventTask task = new EventTask(activity) {
            @Override
            public void onSuccess(List<EventItemResponseModel> responseModel) {
                for (EventItemResponseModel event : responseModel) {
                    EventEntity entity = new EventEntity();
                    entity.setId(event.getId_event());
                    entity.setStore_id(event.getId_store());
                    entity.setEvent_name(event.getNama_event());
                    entity.setStore_name(event.getNama_store());
                    entity.setLocation(event.getLocation());
                    entity.setDate_start(event.getTanggal_start());
                    entity.setDate_end(event.getTanggal_end());
                    entity.setTime_start(event.getWaktu_start());
                    entity.setTime_end(event.getWaktu_end());
                    entity.setDescription(event.getDeskripsi());
                    entity.setImage(event.getGambar());
                    entity.setLs_image(event.getLs_gambar());
                    eventController.insert(entity);
                }

                getLocalPromo();

                if (progress.isShowing())
                    progress.dismiss();
            }

            @Override
            public void onEmpty() {
                emptyCard.setVisibility(View.VISIBLE);
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

    private void getLocalPromo() {
        List<EventEntity> events = eventController.getEvents();
        emptyCard.setVisibility(events.size() > 0 ? View.GONE : View.VISIBLE);

        if (events.size() > 0)
            data.clear();

        data.addAll(events);
        adapter.notifyDataSetChanged();
    }
}
