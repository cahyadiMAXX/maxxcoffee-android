package com.maxxcoffee.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.CardDesignAdapter;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.ui.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.ui.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.model.CardDesignModel;
import com.maxxcoffee.mobile.model.request.CardDesignRequestModel;
import com.maxxcoffee.mobile.model.response.CardImageResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.task.card.CardImageTask;
import com.maxxcoffee.mobile.task.card.ChangeCardDesignTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/19/16.
 */
public class ChangeCardDesignActivity extends FragmentActivity {

    @Bind(R.id.cardmain)
    ImageView cardmain;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.point)
    TextView beans;
    @Bind(R.id.myRecycler)
    RecyclerView recyclerView;

    private CardController cardController;
    private LinearLayoutManager layoutManager;
    private CardDesignAdapter adapter;
    private List<CardDesignModel> list;
    String thisCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_card_design);

        ButterKnife.bind(this);

        list = new ArrayList<>();
        cardController = new CardController(getApplicationContext());
        adapter = new CardDesignAdapter(getApplicationContext(), list) {
            @Override
            public void onCardDesignClick(CardDesignModel model) {
                Glide.with(ChangeCardDesignActivity.this)
                        .load(model.getImageUrl())
                        //.centerCrop()
                        .placeholder(getResources().getDrawable(R.drawable.ic_no_image))
                        //.crossFade()
                        .into(cardmain);
            }
        };

        layoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        Bundle bundle = getIntent().getExtras();
        thisCardId = bundle.getString("card-number", "-1");

        fetchData(bundle);
    }

    void fetchData(Bundle bundle){
        final String cardId = bundle.getString("card-number", "-1");
        final CardEntity card = cardController.getCardByCardNumber(cardId);

        Glide.with(this)
                .load(card.getImage())
                .centerCrop()
                .placeholder(getResources().getDrawable(R.drawable.ic_no_image))
                .crossFade()
                .into(cardmain);
        name.setText(card.getName());
        balance.setText(String.valueOf(card.getBalance()));
        beans.setText(String.valueOf(card.getPoint()));

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);

        CardImageTask task = new CardImageTask(getApplicationContext()) {
            @Override
            public void onSuccess(CardImageResponseModel response) {
                parsingData(response);
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute();
    }

    void parsingData(CardImageResponseModel response){
        list.clear();
        ArrayList<String> cardimages = response.getCard_image();
        ArrayList<String> cardUrls = response.getCard_image_url();

        for(int i = 0; i < cardimages.size(); i++){
            CardDesignModel cd = new CardDesignModel();
            cd.setImageUrl(cardUrls.get(i));
            cd.setImageName(cardimages.get(i));
            list.add(cd);
        }

        adapter.setData(list);
    }

    @OnClick(R.id.changeDesign)
    void onChangeDesignClick(){
        if(adapter.getmSelectedItem() == -1){
            Toast.makeText(getApplicationContext(), "Please select your card design", Toast.LENGTH_LONG).show();
        }else{
            Log.d("selecteditem", adapter.getItemSelected().toString());
            //set prime card
            Bundle bundle = new Bundle();
            bundle.putString("content", "Set this design as your card design ?");

            OptionDialog optionDialog = new OptionDialog() {
                @Override
                protected void onOk() {
                    dismiss();
                    chageCardDesign(adapter.getItemSelected());
                }

                @Override
                public void onCancelClick() {
                    super.onCancelClick();
                    dismiss();
                }
            };
            optionDialog.setArguments(bundle);
            optionDialog.show(getSupportFragmentManager(), null);
        }
    }

    void chageCardDesign(CardDesignModel cd){
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getSupportFragmentManager(), null);

        CardDesignRequestModel body = new CardDesignRequestModel();
        body.setCard_image(cd.getImageName());
        body.setCard_number(thisCardId);

        Log.d("cardbody", body.toString());

        ChangeCardDesignTask task = new ChangeCardDesignTask(getApplicationContext()) {
            @Override
            public void onSuccess(DefaultResponseModel response) {
                finish();
                progress.dismissAllowingStateLoss();
                Toast.makeText(getApplicationContext(), "Your card design successfully changed", Toast.LENGTH_LONG).show();
                //onBackClick();
            }

            @Override
            public void onFailed() {
                finish();
                progress.dismissAllowingStateLoss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }
        };
        task.execute(body);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.back)
    public void onBackClick() {
        onBackPressed();
    }

}
