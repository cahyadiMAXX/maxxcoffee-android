package com.maxxcoffee.mobile.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MyCardDetailFragment extends Fragment {

    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.point)
    TextView point;
    @Bind(R.id.beans)
    TextView beans;
    @Bind(R.id.exp_date)
    TextView expDate;
    @Bind(R.id.card_image)
    ImageView cardImage;

    private MainActivity activity;
    private CardController cardController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
        cardController = new CardController(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_card, container, false);

        ButterKnife.bind(this, view);

        Integer cardId = getArguments().getInt("card-id");

        CardEntity cardModel = cardController.getCard(cardId);
        String mTitle = cardModel.getName();
        Integer mBalance = cardModel.getBalance();
        Integer mPoint = cardModel.getPoint();
        Integer mBean = cardModel.getBeans();
        Integer mImage = cardModel.getImage();
        String mExpDate = cardModel.getExpDate();

        activity.setTitle(mTitle);
        balance.setText("IDR " + mBalance);
        point.setText(mPoint + " points");
        beans.setText(mBean + " beans");
        expDate.setText(mExpDate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cardImage.setImageDrawable(activity.getResources().getDrawable(mImage, null));
        } else {
            cardImage.setImageDrawable(activity.getResources().getDrawable(mImage));
        }
        return view;
    }

    @OnClick(R.id.topup)
    public void onTopupClick() {
    }

    @OnClick(R.id.history)
    public void onHistoryClick() {
    }

}
