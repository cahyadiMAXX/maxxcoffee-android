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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.CardAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.CardMaxDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

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
    private List<CardModel> data;
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
            public void onCardSelected(CardModel model) {
                Bundle bundle = new Bundle();
                bundle.putInt("card-id", model.getId());

                activity.switchFragment(MainActivity.DETAIL_CARD, bundle);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Card");

        layoutManager = new CustomLinearLayoutManager(activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

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
        List<CardEntity> cards = cardController.getCards();
        emptyCard.setVisibility(cards.size() > 0 ? View.GONE : View.VISIBLE);

        data.clear();
        for (CardEntity card : cards) {
            CardModel model = new CardModel();
            model.setId(card.getId());
            model.setName(card.getName());
            model.setBalance(card.getBalance());
            model.setPoint(card.getPoint());
            model.setBeans(card.getBeans());
            model.setImage(card.getImage());

            data.add(model);
        }
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
        }
    }
}
