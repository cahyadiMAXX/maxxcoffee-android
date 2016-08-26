package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.request.RegisterCardRequestModel;
import com.maxxcoffee.mobile.task.RegisterCardTask;

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
                progress.dismissAllowingStateLoss();

                activity.onBackClick();

//                for (CardItemResponseModel card : responseModel) {
//                    CardEntity entity = new CardEntity();
//                    entity.setId(card.getId_card());
//                    entity.setName(card.getCard_name());
//                    entity.setNumber(card.getCard_number());
//                    entity.setImage(card.getCard_image());
//                    entity.setDistribution_id(card.getDistribution_id());
//                    entity.setCard_pin(card.getCard_pin());
//                    entity.setBalance(card.getBalance());
//                    entity.setPoint(card.getBeans());
//                    entity.setExpired_date(card.getExpired_date());
//
//                    cardController.insert(entity);
//
//                    activity.onBackClick();
//                }
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute(body);
    }

    private boolean isFormValid() {
        if (cardNo.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill your card number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
