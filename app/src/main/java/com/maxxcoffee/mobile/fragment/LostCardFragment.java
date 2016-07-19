package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.fragment.dialog.LostCardDialog;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.CardModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class LostCardFragment extends Fragment {

    @Bind(R.id.subject)
    TextView subject;
    @Bind(R.id.card)
    TextView card;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.layout_card)
    LinearLayout layoutCard;

    private MainActivity activity;
    private Integer selectedCard;
    private List<CardModel> data;
//    private CardController cardController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        data = new ArrayList<>();
//        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Report Lost Card");

//        List<CardEntity> cards = cardController.getCards();
//        for (CardEntity card : cards) {
//            CardModel model = new CardModel();
//            model.setId(card.getId());
//            model.setName(card.getName());
//            model.setImage(card.getImage());
//            model.setBalance(card.getBalance());
//            model.setBeans(card.getBeans());
//            model.setExpDate(card.getExpDate());
//            model.setPoint(card.getPoint());
//
//            data.add(model);
//        }

        return view;
    }

    @OnClick(R.id.card)
    public void onCardClick() {
        LostCardDialog lostCardDialog = new LostCardDialog() {
            @Override
            protected void onOk(Integer index) {
                if (index == CARD_1) {
                    setCard(data.get(0));
                } else if (index == CARD_2) {
                    setCard(data.get(1));
                } else if (index == CARD_3) {
                    setCard(data.get(2));
                }
                dismiss();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };

        String cardString = new Gson().toJson(data);

        Bundle bundle = new Bundle();
        bundle.putInt("selected-report", LostCardDialog.CARD_1);
        bundle.putString("cards", cardString);

        lostCardDialog.setArguments(bundle);
        lostCardDialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.arrow_card)
    public void onCardArrowClick() {
        onCardClick();
    }

    @OnClick(R.id.report)
    public void onReportClick() {
        Bundle bundle = new Bundle();
        bundle.putString("content", "Your card will be automatically deactived. Are you sure?");
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                reportLostCardNow();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getFragmentManager(), null);
    }

    private void reportLostCardNow() {

    }

    private void setCard(CardModel model) {
        selectedCard = model.getId();
        card.setText(model.getName());
    }
}
