package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.request.RegisterCardRequestModel;
import com.maxxcoffee.mobile.task.RegisterCardTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/19/16.
 */
public class RenameCardFragment extends Fragment {

    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.cardNo)
    EditText cardNo;

    private FormActivity activity;
    private CardController cardController;

    String tempName;
    String tempNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        cardController = new CardController(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rename_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Add New Card");

        String cardNumber = getArguments().getString("card-no", "");
        if (!cardNumber.equals("-999")) {
            tempNo = cardNumber;
            cardNo.setEnabled(false);
            cardNo.setText(cardNumber);
        }

        int cardSize = cardController.getCards().size();
        tempName = "MY CARD #" + (cardSize + 1);

        return view;
    }

    @OnClick(R.id.done)
    public void onDoneClick() {
        if (!isFormValid())
            return;

        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }

        tempNo = cardNo.getText().toString();
        String cardName = name.getText().equals("") ? tempName : name.getText().toString();

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        RegisterCardRequestModel body = new RegisterCardRequestModel();
        body.setCardNo(tempNo);
        body.setCardName(cardName);

        RegisterCardTask task = new RegisterCardTask(activity) {
            @Override
            public void onSuccess() {
                //sukses, jika akses dari home, maka akan refresh home (jika akses dari home lho ya)
                Toast.makeText(activity, "Card successfully added", Toast.LENGTH_LONG).show();
                PreferenceManager.putBool(getActivity(), Constant.PREFERENCE_ROUTE_CARD_SUCCESS, true);
                PreferenceManager.putInt(getActivity(), Constant.PREFERENCE_CARD_AMOUNT, 1);
                progress.dismissAllowingStateLoss();
                backToOrigin();
            }

            @Override
            public void onFailed() {
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                progress.dismissAllowingStateLoss();
                //failed ga usah back kan ?
                //backToOrigin();
            }
        };
        task.execute(body);
    }

    private void backToOrigin(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                PreferenceManager.putBool(activity, Constant.PREFERENCE_CARD_IS_LOADING, false);
                SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
                Date today = new Date();
                String strToday = df.format(today);
                PreferenceManager.putString(getActivity(), Constant.PREFERENCE_CARD_LAST_UPDATE, strToday);
                activity.finish();
            }
        }, 1500);
    }

    private boolean isFormValid() {
        if (cardNo.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill your card number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
