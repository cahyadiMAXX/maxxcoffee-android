package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.AddCardBarcodeActivity;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.CardAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.CardMaxDialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.task.CardTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
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

    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
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
        activity.setHeaderColor(false, true);

        cardController = new CardController(activity);
        data = new ArrayList<>();
        adapter = new CardAdapter(activity, data) {
            @Override
            public void onCardSelected(CardEntity model) {
                Bundle bundle = new Bundle();
                bundle.putString("card-id", String.valueOf(model.getId()));
//                activity.switchFragment(MainActivity.DETAIL_CARD, bundle);
                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.DETAIL_CARD);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        activity.getRefresh().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipe.setRefreshing(false);
                getLocalCard();
                fetchingData();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean routeToTransfer = PreferenceManager.getBool(activity, Constant.PREFERENCE_ROUTE_TO_TRANSFER_BALANCE, false);
        if (routeToTransfer) {
            activity.switchFragment(MainActivity.BALANCE_TRANSFER);
            PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_TO_TRANSFER_BALANCE, false);
        } else {
            fetchingData();
        }
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

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                getLocalCard();
                fetchingData();
            }
        });

        /*swipe.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    getLocalCard();
                    fetchingData();
                }
            }
        });*/

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
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        CardTask task = new CardTask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {

                if (responseModel.size() > 0)
                    cardController.clear();

                for (CardItemResponseModel card : responseModel) {
                    CardEntity entity = new CardEntity();
                    entity.setId(card.getId_card());
                    entity.setName(card.getCard_name());
                    entity.setNumber(card.getCard_number());
                    entity.setImage(card.getCard_image());
                    entity.setDistribution_id(card.getDistribution_id());
                    entity.setCard_pin(card.getCard_pin());
                    entity.setBalance(card.getBalance());
                    entity.setPoint(card.getBeans());
                    entity.setBarcode(card.getBarcode());
                    entity.setExpired_date(card.getExpired_date());

                    cardController.insert(entity);
                }
                getLocalCard();
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute();
    }

    private void getLocalCard() {
        List<CardEntity> cards = cardController.getCards();
        emptyCard.setVisibility(cards.size() > 0 ? View.GONE : View.VISIBLE);

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
                    Intent intent = new Intent(activity, FormActivity.class);
                    intent.putExtra("content", FormActivity.DELETE_CARD);
                    startActivity(intent);

                    disableLayer.setVisibility(View.GONE);
                    fabMenu.close(true);
                    dismiss();
                }
            };
            dialog.show(getFragmentManager(), null);
        } else {
            disableLayer.setVisibility(View.GONE);
            fabMenu.close(true);
            Intent intent = new Intent(activity, AddCardBarcodeActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fab_virtual)
    public void onVirtualClick() {
        disableLayer.setVisibility(View.GONE);
        fabMenu.close(true);

        Toast.makeText(activity, "This feature is not available yet", Toast.LENGTH_SHORT).show();
    }
}
