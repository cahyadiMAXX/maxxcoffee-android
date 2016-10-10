package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.AddCardBarcodeActivity;
import com.maxxcoffee.mobile.activity.AddVirtualCardActivity;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.CardAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.CardMaxDialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.task.CardCGITask;
import com.maxxcoffee.mobile.task.CardTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MyCardFragment extends Fragment {

    //@Bind(R.id.swipe)
    //SwipeRefreshLayout swipe;
    @Bind(R.id.card_list)
    RecyclerView recyclerView;
    @Bind(R.id.empty_card)
    TextView emptyCard;
    @Bind(R.id.fab_menu)
    FloatingActionMenu fabMenu;
    @Bind(R.id.disable_layer)
    FrameLayout disableLayer;
    @Bind(R.id.fab_primary)
    FloatingActionButton fab_primary;

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
                bundle.putString("card-number", String.valueOf(model.getNumber()));
                bundle.putString("card-is-virtual", String.valueOf(model.getVirtual_card()));
                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.DETAIL_CARD);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        try{
            activity.getRefresh().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //swipe.setRefreshing(false);
                    if (fabMenu.isOpened()) {
                        disableLayer.setVisibility(View.GONE);
                        fabMenu.close(true);
                    }
                    getLocalCard();
                    fetchingData();
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(), "masuk on resume coyy", Toast.LENGTH_LONG).show();
        boolean routeToTransfer = PreferenceManager.getBool(activity, Constant.PREFERENCE_ROUTE_TO_TRANSFER_BALANCE, false);
        if (routeToTransfer) {
            activity.switchFragment(MainActivity.BALANCE_TRANSFER);
            PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_TO_TRANSFER_BALANCE, false);
        } else {
            checkToLoadCard();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("My Card");

        layoutManager = new CustomLinearLayoutManager(activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        checkToLoadCard();

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    disableLayer.setVisibility(View.GONE);
                    fabMenu.close(true);
                } else {
                    if(data.size() == 0){
                        fab_primary.setVisibility(View.GONE);
                    }else{
                        fabMenu.setVisibility(View.VISIBLE);
                    }
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

        fab_primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMenu.isOpened()) {
                    disableLayer.setVisibility(View.GONE);
                    fabMenu.close(true);
                } else {
                    disableLayer.setVisibility(View.VISIBLE);
                    fabMenu.open(true);
                }
                if(Utils.isConnected(activity)){
                    Intent in = new Intent(activity, FormActivity.class);
                    in.putExtra("content", FormActivity.PRIMARY_CARD);
                    startActivity(in);
                }else{
                    Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void checkToLoadCard(){
        boolean isLoaded = PreferenceManager.getBool(getActivity(), Constant.PREFERENCE_CARD_IS_LOADING, false);
        if(!isLoaded){
            fetchingData();
        }else{
            SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
            Date today = new Date();
            String strToday = df.format(today);
            String lastUpdate = PreferenceManager.getString(getActivity(), Constant.PREFERENCE_CARD_LAST_UPDATE, strToday);
            Date inputDate = null;
            try {
                inputDate = df.parse(lastUpdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //klo lebih dari 30 menit, baru ambil lagi (atau refresh sendiri)
           // Toast.makeText(getActivity(), "Waktu kita: " + String.valueOf(Utils.getDurationInMinutes(inputDate)), Toast.LENGTH_LONG).show();
            if(Utils.getDurationInMinutes(inputDate) > 30){
                fetchingData();
            }else{
                getLocalCard();
            }
        }
    }


    private void fetchingData() {
        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        PreferenceManager.putBool(getActivity(), Constant.PREFERENCE_CARD_IS_LOADING, true);
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
        Date today = new Date();
        String strToday = df.format(today);
        PreferenceManager.putString(getActivity(), Constant.PREFERENCE_CARD_LAST_UPDATE, strToday);

        CardCGITask task = new CardCGITask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {
                //Log.d("responseModel", responseModel.toString());

                PreferenceManager.putInt(activity, Constant.PREFERENCE_CARD_AMOUNT, responseModel.size());

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
                    entity.setPrimary(card.getPrimary());
                    entity.setVirtual_card(card.getVirtual_card());

                    cardController.insert(entity);
                }
                getLocalCard();
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed(String message) {
                //Toast.makeText(getActivity(), getResources().getString(R.string.no_card_alert), Toast.LENGTH_LONG).show();
                PreferenceManager.putInt(activity, Constant.PREFERENCE_CARD_AMOUNT, 0);
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                Toast.makeText(getActivity(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
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
        if (cards.size() >= 3) {
            CardMaxDialog dialog = new CardMaxDialog() {

                @Override
                protected void onDeleteCard() {
                    if(Utils.isConnected(activity)){
                        Intent intent = new Intent(activity, FormActivity.class);
                        intent.putExtra("content", FormActivity.DELETE_CARD);
                        startActivity(intent);
                    }else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
                    }

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
