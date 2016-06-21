package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.PromoAdapter;
import com.maxxcoffee.mobile.database.controller.PromoController;
import com.maxxcoffee.mobile.database.entity.PromoEntity;
import com.maxxcoffee.mobile.fragment.dialog.PromoDialog;
import com.maxxcoffee.mobile.model.PromoModel;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class PromoFragment extends Fragment {

    @Bind(R.id.recycleview)
    RecyclerView recyclerView;

    private MainActivity activity;
    private List<PromoModel> data;
    private PromoAdapter adapter;
    private PromoController promoController;
    private CustomLinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        promoController = new PromoController(activity);
        data = new ArrayList<>();
        adapter = new PromoAdapter(activity, data) {
            @Override
            public void onCardClick(PromoModel model) {
                Bundle bundle = new Bundle();
                bundle.putString("title", model.getTitle());
                bundle.putString("desc", model.getDescription());
                bundle.putInt("image", model.getImage());

                PromoDialog dialog = new PromoDialog();
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), null);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Promo");

        layoutManager = new CustomLinearLayoutManager(activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fetchingData();

        return view;
    }

    private void fetchingData() {
        List<PromoEntity> promos = promoController.getPromos();
        for (PromoEntity promo : promos) {
            PromoModel model = new PromoModel();
            model.setId(promo.getId());
            model.setTitle(promo.getTitle());
            model.setDescription(promo.getDescription());
            model.setImage(promo.getImage());

            data.add(model);
        }
        adapter.notifyDataSetChanged();
    }
}
