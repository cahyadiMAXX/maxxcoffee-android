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
import com.maxxcoffee.mobile.activity.MoreDetailActivity;
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
import com.maxxcoffee.mobile.util.Utils;

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

    private MoreDetailActivity activity;
    private CardController cardController;
    private List<CardModel> data;
    private SimpleDateFormat dateFormat;
    private String token;
    private String selectedCard;
    private String selectedStartDate;
    private String selectedEndDate;

    public CardDetailHistoryFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MoreDetailActivity) getActivity();

        data = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cardController = new CardController(activity);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Card History");

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

        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String startMonth = (month < 10 ? "0" + String.valueOf(month): String.valueOf(month));
        String endMonth = (month + 1 < 10 ? "0" + String.valueOf(month + 1): String.valueOf(month + 1));
        String fixDay = (day < 10 ? "0" + String.valueOf(day): String.valueOf(day));

        periodeStart.setText(year + "-" + startMonth + "-" + fixDay);
        periodeEnd.setText(year + "-" + endMonth + "-" + fixDay);
    }

    @OnClick(R.id.start_period_layout)
    public void onStartPeriodeClick() {
        DateDialog datePicker = new DateDialog(activity, BirthdateDialog.DATE_VALIDARION_OFF) {
            @Override
            protected void onDateSelected(Date date) {
                if (date != null) {
                    try {
                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date end = myFormat.parse(periodeEnd.getText().toString());
                        if(date.before(end)){
                            selectedStartDate = dateFormat.format(date);
                            periodeStart.setText(selectedStartDate);
                        }else{
                            Toast.makeText(getActivity(), "Start Date must be earlier than End Date", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){

                    }
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
                    try {
                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date start = myFormat.parse(periodeStart.getText().toString());
                        if(date.after(start)){
                            selectedEndDate = dateFormat.format(date);
                            periodeEnd.setText(selectedEndDate);
                        }else{
                            Toast.makeText(getActivity(), "Start Date must be earlier than End Date", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){

                    }
                }
                dismiss();
            }

            @Override
            protected void onError(String message) {
            }
        };
        datePicker.show(getFragmentManager(), null);
    }


    @OnClick(R.id.search)
    public void onSearchClick() {
        if (!isValidForm())
            return;

        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }
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
        //initDate();
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        HistoryRequestModel body = new HistoryRequestModel();
        body.setCard_number(selectedCard);
        body.setPeriode_start(periodeStart.getText().toString());
        body.setPeriode_end(periodeEnd.getText().toString());

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
