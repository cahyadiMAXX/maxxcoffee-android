package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.j256.ormlite.stmt.query.In;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.PrimaryCardAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.OkDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.FaqModel;
import com.maxxcoffee.mobile.model.request.PrimaryCardRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.task.CardBalanceTask;
import com.maxxcoffee.mobile.task.CardTask;
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.task.SetPrimaryCardTask;
import com.maxxcoffee.mobile.task.TosTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */

public class PrimaryCardFragment extends Fragment {

    @Bind(R.id.myRecycler)
    RecyclerView recyclerView;
    @Bind(R.id.btnSetPrimary)
    Button btnSetPrimary;

    private FormActivity activity;
    private CardController cardController;
    private PrimaryCardAdapter adapter;
    private List<CardModel> data;
    private CustomLinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        data = new ArrayList<>();
        cardController = new CardController(activity);
        adapter = new PrimaryCardAdapter(activity, data) {
            @Override
            public void onFaqClick(CardModel model) {

            }
        };
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_primary_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Set Prime Card");

        layoutManager = new CustomLinearLayoutManager(activity);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        btnSetPrimary.setVisibility(View.GONE);

        fetchingData();

        btnSetPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getmSelectedItem() == -1){
                    Toast.makeText(activity, "Please select your card !", Toast.LENGTH_LONG).show();
                }else{
                    Log.d("selecteditem", adapter.getItemSelected().toString());
                    //set prime card
                    Bundle bundle = new Bundle();
                    bundle.putString("content", "Set this card as your Prime Card ?");

                    OptionDialog optionDialog = new OptionDialog() {
                        @Override
                        protected void onOk() {
                            setPrimaryCard(adapter.getItemSelected());
                            dismiss();
                        }

                        @Override
                        public void onCancelClick() {
                            super.onCancelClick();
                            dismiss();
                        }
                    };
                    optionDialog.setArguments(bundle);
                    optionDialog.show(getFragmentManager(), null);
                }
            }
        });

        return view;
    }

    private void setPrimaryCard(final CardModel card){
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        PrimaryCardRequestModel body = new PrimaryCardRequestModel();
        body.setCard_number(card.getNumber());

        SetPrimaryCardTask task = new SetPrimaryCardTask(activity) {
            @Override
            public void onSuccess(DefaultResponseModel responseModel) {
                if(responseModel.getStatus().equals("success")){
                    progress.dismissAllowingStateLoss();
                    //redirect ke card, suruh mainactivity refresh card
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_CARD_IS_LOADING, false);
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_CARD_SUCCESS, true);
                    Toast.makeText(activity, "Prime Card successfully set", Toast.LENGTH_LONG).show();
                    activity.onBackClick();
                }
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }
        };
        task.execute(body);
    }

    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        CardTask task = new CardTask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {
                btnSetPrimary.setVisibility(View.VISIBLE);

                for (CardItemResponseModel card : responseModel) {
                    CardModel entity = new CardModel();
                    entity.setId(card.getId_card());
                    entity.setName(card.getCard_name());
                    entity.setNumber(card.getCard_number());
                    entity.setImage(card.getCard_image());
                    entity.setDistribution_id(card.getDistribution_id());
                    entity.setCard_pin(card.getCard_pin());
                    entity.setBalance(card.getBalance());
                    entity.setPoint(card.getBeans());
                    entity.setExpired_date(card.getExpired_date());
                    entity.setPrimary(card.getPrimary());

                    if(card.getPrimary() == 0){
                        data.add(entity);
                    }
                }
                adapter.notifyDataSetChanged();
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(activity, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute();
    }
}
