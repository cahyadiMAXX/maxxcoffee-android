package com.maxxcoffee.mobile.fragment.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.CardModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 3/13/2016.
 */
public class LostCardDialog extends DialogFragment {

    public static Integer CARD_1 = 0;
    public static Integer CARD_2 = 1;
    public static Integer CARD_3 = 2;

    @Bind(R.id.card_1)
    TextView card1;
    @Bind(R.id.card_2)
    TextView card2;
    @Bind(R.id.card_3)
    TextView card3;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.layout_card_2)
    LinearLayout layoutCard2;
    @Bind(R.id.layout_card_3)
    LinearLayout layoutCard3;

    Integer selectedReport;
    List<CardModel> data;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lost_card);
        dialog.show();

        ButterKnife.bind(this, dialog);

        data = new ArrayList<>();
        selectedReport = getArguments().getInt("selected-card", -999);
        String stringData = getArguments().getString("cards", "");

        if (stringData != null) {
            data = new Gson().fromJson(stringData, new TypeToken<List<CardModel>>() {
            }.getType());
        }

        for (int i = 0; i < data.size(); i++) {
            CardModel model = data.get(i);
            if (i == 0) {
                card1.setText(model.getName());
            } else if (i == 1) {
                layoutCard2.setVisibility(View.VISIBLE);
                card2.setText(model.getName());
            } else if (i == 2) {
                layoutCard3.setVisibility(View.VISIBLE);
                card3.setText(model.getName());
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            card1.setTextColor(getResources().getColor(R.color.green_selected, null));
        } else {
            card1.setTextColor(getResources().getColor(R.color.green_selected));
        }


        ok.setTextColor(Color.RED);
        cancel.setTextColor(Color.BLACK);

        return dialog;
    }

    @OnClick(R.id.card_1)
    public void onCard1Click() {
        selectedReport = CARD_1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            card1.setTextColor(getResources().getColor(R.color.green_selected, null));
            card2.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            card3.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        } else {
            card1.setTextColor(getResources().getColor(R.color.green_selected));
            card2.setTextColor(getResources().getColor(android.R.color.darker_gray));
            card3.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @OnClick(R.id.card_2)
    public void onCard2Click() {
        selectedReport = CARD_2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            card1.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            card2.setTextColor(getResources().getColor(R.color.green_selected, null));
            card3.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        } else {
            card1.setTextColor(getResources().getColor(android.R.color.darker_gray));
            card2.setTextColor(getResources().getColor(R.color.green_selected));
            card3.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @OnClick(R.id.card_3)
    public void onCard3Click() {
        selectedReport = CARD_3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            card1.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            card2.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            card3.setTextColor(getResources().getColor(R.color.green_selected, null));
        } else {
            card1.setTextColor(getResources().getColor(android.R.color.darker_gray));
            card2.setTextColor(getResources().getColor(android.R.color.darker_gray));
            card3.setTextColor(getResources().getColor(R.color.green_selected));
        }
    }

    @OnClick(R.id.ok)
    public void onOkClick() {
        onOk(selectedReport);
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        onCancel();
    }

    protected  void onOk(Integer index){}

    protected  void onCancel(){}
}
