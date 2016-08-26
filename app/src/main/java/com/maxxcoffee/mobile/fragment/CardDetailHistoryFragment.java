package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.adapter.DetailHistoryPagerAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.fragment.dialog.BirthdateDialog;
import com.maxxcoffee.mobile.fragment.dialog.DateDialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.CardModel;
import com.maxxcoffee.mobile.model.request.HistoryRequestModel;
import com.maxxcoffee.mobile.task.HistoryTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class CardDetailHistoryFragment extends Fragment {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.history_layout)
    LinearLayout historyLayout;
    @Bind(R.id.periode_start)
    TextView periodeStart;
    @Bind(R.id.periode_end)
    TextView periodeEnd;
    @Bind(R.id.card)
    TextView card;

    private FormActivity activity;
    private CardController cardController;
    private List<CardModel> data;
    private SimpleDateFormat dateFormat;
    private String token;
    private String selectedCard;
    private String selectedStartDate;
    private String selectedEndDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();

        data = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cardController = new CardController(activity);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("History");

        String cardName = getArguments().getString("card-name", "");
        selectedCard = getArguments().getString("card-number", "");

        card.setText(cardName);
        initDate();

        tabs.post(new Runnable() {
            @Override
            public void run() {
                setupViewPager(viewPager);
                tabs.setupWithViewPager(viewPager);
            }
        });
        fetchingData();

        return view;
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        periodeStart.setText(year + "-" + month + "-" + day);
        periodeEnd.setText(year + "-" + (month + 1) + "-" + day);
    }

    @OnClick(R.id.start_period_layout)
    public void onStartPeriodeClick() {
        DateDialog datePicker = new DateDialog(activity, BirthdateDialog.DATE_VALIDARION_OFF) {
            @Override
            protected void onDateSelected(Date date) {
                if (date != null) {
                    selectedStartDate = dateFormat.format(date);
                    periodeStart.setText(selectedStartDate);
                }
                dismiss();
            }

            @Override
            protected void onError(String message) {
            }
        };
        datePicker.show(getFragmentManager(), null);
    }

    @OnClick(R.id.end_periode_layout)
    public void onEndPeriodeClick() {
        DateDialog datePicker = new DateDialog(activity, BirthdateDialog.DATE_VALIDARION_OFF) {
            @Override
            protected void onDateSelected(Date date) {
                if (date != null) {
                    selectedEndDate = dateFormat.format(date);
                    periodeEnd.setText(selectedEndDate);
                }
                dismiss();
            }

            @Override
            protected void onError(String message) {
            }
        };
        datePicker.show(getFragmentManager(), null);
    }

//    @OnClick(R.id.card_layout)
//    public void onCardLayoutClick() {
//        LostCardDialog lostCardDialog = new LostCardDialog() {
//            @Override
//            protected void onOk(Integer index) {
//                if (index == CARD_1) {
//                    setCard(data.get(0));
//                } else if (index == CARD_2) {
//                    setCard(data.get(1));
//                } else if (index == CARD_3) {
//                    setCard(data.get(2));
//                }
//                dismiss();
//            }
//
//            @Override
//            protected void onCancel() {
//                dismiss();
//            }
//        };
//
//        String cardString = new Gson().toJson(data);
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("selected-report", LostCardDialog.CARD_1);
//        bundle.putString("cards", cardString);
//
//        lostCardDialog.setArguments(bundle);
//        lostCardDialog.show(getFragmentManager(), null);
//    }

    @OnClick(R.id.search)
    public void onSearchClick() {
        if (!isValidForm())
            return;

        fetchingData();
    }

    private boolean isValidForm() {
        if (periodeStart.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill start period", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (periodeEnd.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill end period", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (card.getText().toString().equals("")) {
            Toast.makeText(activity, "Please select card", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

//    private void fetchingCardData() {
//        final TBaseProgress progress = new TBaseProgress(activity);
//        progress.show();
//
//        CardTask task = new CardTask(activity) {
//            @Override
//            public void onSuccess(List<CardItemResponseModel> responseModel) {
//                for (CardItemResponseModel card : responseModel) {
//                    CardEntity entity = new CardEntity();
////                    entity.setId(card.getId_card());
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
//                }
//                getLocalCard();
//
//
//                    progress.dismiss();
//            }
//
//            @Override
//            public void onFailed() {
//
//                    progress.dismiss();
//            }
//        };
//        task.execute();
//    }

    private void getLocalCard() {
        data.clear();
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

            data.add(model);
        }
    }

    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        HistoryRequestModel body = new HistoryRequestModel();
        body.setCard_number(selectedCard);
        body.setPeriode_start(selectedStartDate);
        body.setPeriode_end(selectedEndDate);

        HistoryTask task = new HistoryTask(activity) {
            @Override
            public void onSuccess() {
                    progress.dismissAllowingStateLoss();

                historyLayout.setVisibility(View.VISIBLE);
                setupViewPager(viewPager);
                tabs.setupWithViewPager(viewPager);
            }

            @Override
            public void onFailed() {
                    progress.dismiss();
            }
        };
        task.execute(body);
    }

    private void setCard(CardModel model) {
        selectedCard = model.getNumber();
        card.setText(model.getName());
    }

    private void setupViewPager(ViewPager viewPager) {
        DetailHistoryPagerAdapter adapter = new DetailHistoryPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
