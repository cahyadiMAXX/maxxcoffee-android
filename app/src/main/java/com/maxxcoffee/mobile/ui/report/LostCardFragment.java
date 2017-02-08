package com.maxxcoffee.mobile.ui.report;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.ui.fragment.dialog.LostCardDialog;
import com.maxxcoffee.mobile.ui.fragment.dialog.LostCardSubjectDialog;
import com.maxxcoffee.mobile.ui.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.request.LostCardRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.task.card.CardTask;
import com.maxxcoffee.mobile.task.card.LostCardTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

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

    @Bind(R.id.mainframe)
    LinearLayout mainframe;
    @Bind(R.id.empty)
    TextView empty;

    private MainActivity activity;
    private List<CardModel> data;
    private CardController cardController;
    private String selectedCard = "";
    private Integer selectedSubject;
    private Integer selectedCrd;
//    private Integer selectedCard;

    Dialog loading;

    public LostCardFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);

        data = new ArrayList<>();
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Report Lost Card");

        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);

        mainframe.setVisibility(View.GONE);

        int virtual = PreferenceManager.getInt(getActivity(), Constant.PREFERENCE_HAS_VIRTUAL_CARD, 0);
        if(virtual == 0){
            if(Utils.isConnected(activity)){
                fetchingData();
            }else{
                Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            }
        }else{
            empty.setText(getActivity().getResources().getString(R.string.virtual_card));
            empty.setVisibility(View.VISIBLE);
        }

        selectedSubject = -999;
        selectedCrd = -999;

        return view;
    }

    private void fetchingData() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        loading.show();

        CardTask task = new CardTask(activity) {
            @Override
            public void onSuccess(List<CardItemResponseModel> responseModel) {
                if(responseModel.size() > 0){
                    mainframe.setVisibility(View.VISIBLE);
                }else {
                    empty.setVisibility(View.VISIBLE);
                }
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
                    entity.setExpired_date(card.getExpired_date());
                    entity.setVirtual_card(card.getVirtual_card());

                    cardController.insert(entity);
                }

                getLocalCard();
                if (loading.isShowing())loading.dismiss();
                //progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
            }

            @Override
            public void onFailed(String message) {
                empty.setText(message);
                empty.setVisibility(View.VISIBLE);
                mainframe.setVisibility(View.GONE);
                //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
            }
        };
        task.execute();
    }

    private void getLocalCard() {
        List<CardEntity> cards = cardController.getCards();
        for (CardEntity card : cards) {
            CardModel model = new CardModel();
            model.setId(card.getId());
            model.setName(card.getName());
            model.setNumber(card.getNumber());
            model.setImage(card.getImage());
            model.setDistribution_id(card.getDistribution_id());
            model.setCard_pin(card.getCard_pin());
            model.setBalance(card.getBalance());
            model.setPoint(card.getPoint());
            model.setExpired_date(card.getExpired_date());
            model.setVirtual_card(card.getVirtual_card());

            data.add(model);
        }
    }


    @OnClick(R.id.layout_card)
    public void onLayoutCardClick() {
        if (data.size() == 0) {
            Toast.makeText(activity, "You do not have any connected card. \n\nPlease add card.", Toast.LENGTH_SHORT).show();
            return;
        }else if(data.size() == 1 && data.get(0).getVirtual_card() == 1){
            Toast.makeText(activity, "You do not have any physical card to be reported", Toast.LENGTH_SHORT).show();
            return;
        }

        LostCardDialog lostCardDialog = new LostCardDialog() {
            @Override
            protected void onOk(Integer index) {
                if (index == CARD_1) {
                    selectedCrd = CARD_1;
                    setCard(data.get(0));
                } else if (index == CARD_2) {
                    selectedCrd = CARD_2;
                    setCard(data.get(1));
                } else if (index == CARD_3) {
                    selectedCrd = CARD_3;
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
        bundle.putInt("selected-card", selectedCrd);
        bundle.putString("cards", cardString);

        lostCardDialog.setArguments(bundle);
        lostCardDialog.show(getFragmentManager(), null);
    }

//    @OnClick(R.id.arrow_card)
//    public void onCardArrowClick() {
//        onCardClick();
//    }

    @OnClick(R.id.layout_subject)
    public void onSubjectClick() {

        LostCardSubjectDialog dialog = new LostCardSubjectDialog() {
            @Override
            protected void onOk(Integer index) {
                String item = "";
                if (index == LOST) {
                    selectedSubject = LOST;
                    item = "Lost Card";
                } else if (index == BROKEN) {
                    selectedSubject = BROKEN;
                    item = "Broken Card";
                }
                subject.setText(item);
                dismiss();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt("selected-subject", selectedSubject);

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.report)
    public void onReportClick() {
        if (!isFormValid())
            return;

        Bundle bundle = new Bundle();
        bundle.putString("content", "Your card will be automatically deactivated. Are you sure?");
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                dismiss();
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
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog progress;
        progress = new Dialog(getActivity());
        progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progress.setContentView(R.layout.dialog_loading);
        progress.setCancelable(false);
        progress.show();

        String subj = "";
        if (subject.getText().toString().equals("Lost Card")) {
            subj = "lost";
        } else if (subject.getText().toString().equals("Broken Card")) {
            subj = "broken";
        }

        LostCardRequestModel body = new LostCardRequestModel();
        body.setId_card(selectedCard);
        body.setDetail(detail.getText().toString());
        body.setSubject(subj);

        LostCardTask task = new LostCardTask(activity) {
            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())progress.dismiss();
                Toast.makeText(activity, "Your report has been submitted successfully", Toast.LENGTH_SHORT).show();
                detail.setText("");
                activity.switchFragment(MainActivity.MY_CARD);
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                Toast.makeText(getActivity(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                if (loading.isShowing())progress.dismiss();
            }
        };
        task.execute(body);
    }

    private boolean isFormValid() {
        if (selectedCard.equals("")) {
            Toast.makeText(activity, "Please select your card", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (detail.getText().equals("")) {
            Toast.makeText(activity, "Please fill the detail", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setCard(CardModel model) {
        selectedCard = String.valueOf(model.getId());
        card.setText(model.getName());
    }
}
