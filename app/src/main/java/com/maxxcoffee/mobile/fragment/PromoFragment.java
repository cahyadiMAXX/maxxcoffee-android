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
import com.maxxcoffee.mobile.adapter.PromoAdapter;
import com.maxxcoffee.mobile.database.controller.PromoController;
import com.maxxcoffee.mobile.database.entity.PromoEntity;
import com.maxxcoffee.mobile.fragment.dialog.PromoDialog;
import com.maxxcoffee.mobile.model.PromoModel;
import com.maxxcoffee.mobile.model.response.PromoItemResponseModel;
import com.maxxcoffee.mobile.model.response.PromoResponseModel;
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
public class PromoFragment extends Fragment {

    @Bind(R.id.recycleview)
    RecyclerView recyclerView;
    @Bind(R.id.empty_card)
    TextView emptyCard;

    private MainActivity activity;
    private List<PromoEntity> data;
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
            public void onCardClick(PromoEntity model) {
                Bundle bundle = new Bundle();
                bundle.putString("title", model.getName());
                bundle.putString("desc", model.getDescription());
                bundle.putString("image", model.getImage());

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

        getLocalPromo();
        fetchingData();

        return view;
    }

    private void fetchingData() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        PromoTask task = new PromoTask(activity) {
            @Override
            public void onSuccess(List<PromoItemResponseModel> responseModel) {
                for (PromoItemResponseModel promo : responseModel) {
                    PromoEntity entity = new PromoEntity();
                    entity.setId(promo.getId_promo());
                    entity.setName(promo.getNama_promo());
                    entity.setDescription(promo.getDeskripsi());
                    entity.setImage(promo.getGambar());
                    entity.setLs_image(promo.getLs_gambar());
                    entity.setSyarat(promo.getSyarat());
                    entity.setDate_start(promo.getTanggal_start());
                    entity.setDate_end(promo.getTanggal_end());
                    entity.setTime_start(promo.getWaktu_start());
                    entity.setTime_end(promo.getWaktu_end());
                    promoController.insert(entity);
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
        List<PromoEntity> promos = promoController.getPromos();
        emptyCard.setVisibility(promos.size() > 0 ? View.GONE : View.VISIBLE);

        if (promos.size() > 0)
            data.clear();

        data.addAll(promos);
        adapter.notifyDataSetChanged();
    }
}
