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

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.util.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Bind(R.id.exp_date)
    TextView expDate;
    @Bind(R.id.card_image)
    ImageView cardImage;
    @Bind(R.id.beans_bubble)
    TextView beansBubble;
    @Bind(R.id.reward_achieved)
    TextView rewardAchieved;

    private FormActivity activity;
    private CardController cardController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_card, container, false);

        ButterKnife.bind(this, view);

        fetchingCard();

        return view;
    }

    private void fetchingCard() {
        String cardId = getArguments().getString("card-id", "-1");

        CardEntity card = cardController.getCard(cardId);
        if (card != null) {
            try {
                DateFormat mDateFormat = new SimpleDateFormat(Constant.DATEFORMAT_STRING);
                Date mExpDate = new SimpleDateFormat(Constant.DATEFORMAT_META).parse(card.getExpired_date());

                int mBalance = card.getBalance();
                int mPoint = card.getPoint();
                int mRewardAchieve = mPoint / 10;
                int mRewardToGo = 10 - (mPoint % 10);

                activity.setTitle(card.getName());
                balance.setText("IDR " + mBalance);
                point.setText(mPoint + " points");
                expDate.setText(mDateFormat.format(mExpDate));
                beansBubble.setText(String.valueOf(mRewardToGo));
                rewardAchieved.setText(String.valueOf(mRewardAchieve));
                Glide.with(activity).load(card.getImage()).placeholder(activity.getResources().getDrawable(R.drawable.ic_no_image)).crossFade().into(cardImage);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.topup)
    public void onTopupClick() {
    }

    @OnClick(R.id.history)
    public void onHistoryClick() {
    }

}
