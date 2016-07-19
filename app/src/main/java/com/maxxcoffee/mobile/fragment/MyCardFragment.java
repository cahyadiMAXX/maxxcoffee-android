package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.AddCardBarcodeActivity;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.CardAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.CardMaxDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.task.CardTask;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MyCardFragment extends Fragment {

    @Bind(R.id.card_list)
    RecyclerView recyclerView;
    @Bind(R.id.empty_card)
    TextView emptyCard;
    @Bind(R.id.fab_menu)
    FloatingActionMenu fabMenu;
    @Bind(R.id.disable_layer)
    FrameLayout disableLayer;

    private MainActivity activity;
    private List<CardEntity> data;
    private CardAdapter adapter;
    private CardController cardController;
    private CustomLinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        cardController = new CardController(activity);
        data = new ArrayList<>();
        adapter = new CardAdapter(activity, data) {
            @Override
            public void onCardSelected(CardEntity model) {
                Bundle bundle = new Bundle();
                bundle.putString("card-id", String.valueOf(model.getId()));
//
//                activity.switchFragment(MainActivity.DETAIL_CARD, bundle);
                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.DETAIL_CARD);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Card");

        layoutManager = new CustomLinearLayoutManager(activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getLocalCard();
        fetchingData();

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    disableLayer.setVisibility(View.GONE);
                    fabMenu.close(true);
                } else {
                    disableLayer.setVisibility(View.VISIBLE);
                    fabMenu.open(true);
                }
            }
        });
        disableLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableLayer.setVisibility(View.GONE);
                fabMenu.close(true);
            }
        });

        return view;
    }

    private void fetchingData() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        CardTask task = new CardTask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {
                for (CardItemResponseModel card : responseModel) {
                    CardEntity entity = new CardEntity();
                    entity.setId(card.getId_card());
                    entity.setName(card.getCard_name());
                    entity.setNumber(card.getCard_number());
                    entity.setImage(card.getCard_image());
                    entity.setDistribution_id(card.getDistribution_id());
                    entity.setCard_pin(card.getCard_pin());
                    entity.setBalance(card.getBalance());
                    entity.setPoint(card.getPoint());
                    entity.setExpired_date(card.getExpired_date());

                    cardController.insert(entity);
                }
                getLocalCard();

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

    private void getLocalCard() {
        List<CardEntity> cards = cardController.getCards();
        emptyCard.setVisibility(cards.size() > 0 ? View.GONE : View.VISIBLE);

        if (cards.size() > 0)
            data.clear();

        data.addAll(cards);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab_scan)
    public void onAddClick() {
        List<CardEntity> cards = cardController.getCards();
        if (cards.size() == 3) {
            CardMaxDialog dialog = new CardMaxDialog() {

                @Override
                protected void onDeleteCard() {
                    dismiss();
                }
            };
            dialog.show(getFragmentManager(), null);
        } else {
            activity.openBarcode();
            fabMenu.close(true);
        }
    }
}
