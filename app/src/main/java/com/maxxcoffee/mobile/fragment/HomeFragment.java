package com.maxxcoffee.mobile.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.AddCardBarcodeActivity;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardPrimaryController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.CardPrimaryEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.HomeResponseModel;
import com.maxxcoffee.mobile.task.HomeTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.OnSwipeTouchListener;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Bind(R.id.greeting)
    TextView greeting;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.point)
    TextView point;
    @Bind(R.id.credential_buttons)
    LinearLayout credential_buttons;
    @Bind(R.id.button_c_login)
    Button buttonLogin;
    @Bind(R.id.button_c_signup)
    Button buttonSignUp;
    @Bind(R.id.design_bottom_sheet)
    View bottomSheet;
    @Bind(R.id.bottom_sheet_arrow)
    GifImageView bottom_sheet_arrow;

    //belum set primary
    @Bind(R.id.not_set)
    LinearLayout not_set;
    //sudah set primary
    @Bind(R.id.is_set)
    LinearLayout is_set;
    @Bind(R.id.cardName)
    TextView cardName;
    //blm ada kartu
    @Bind(R.id.no_cards)
    LinearLayout no_cards;
    @Bind(R.id.cardBalance)
    TextView cardBalance;
    @Bind(R.id.cardBeans)
    TextView cardBeans;
    @Bind(R.id.cardImage)
    ImageView cardImage;
    @Bind(R.id.buttonSetPrimary)
    Button btnSetPrimary;
    @Bind(R.id.buttonAddCard)
    Button buttonAddCard;

    private MainActivity activity;
    private String token;
    private int mDayPart = Utils.getDayPart();
    private boolean isPrimaryExist = false;
    private CardPrimaryController cardController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        cardController = new CardPrimaryController(activity);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("");
        boolean isLoggedIn = PreferenceManager.getBool(activity, Constant.PREFERENCE_LOGGED_IN, false);
        isPrimaryExist = false;

        //klo udah login
        if(isLoggedIn){
            credential_buttons.setVisibility(View.GONE);
        }

        getLocalData();
        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }
        //fetchingData();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchFragment(MainActivity.LOGIN);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchFragment(MainActivity.SIGNUP);
            }
        });

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        //arrow ke bawah
                        ObjectAnimator animator =  ObjectAnimator.ofFloat(bottom_sheet_arrow,"rotation",0,-180f);
                        animator.setDuration(200);
                        animator.start();
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        //arrow ke atas
                        ObjectAnimator aim =   ObjectAnimator.ofFloat(bottom_sheet_arrow,"rotation",0,0f);
                        aim.setDuration(200);
                        aim.start();
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });

        behavior.setHideable(false);

        bottom_sheet_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnSetPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isConnected(activity)){
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_CARD_SUCCESS, false);
                    Intent in = new Intent(activity, FormActivity.class);
                    in.putExtra("content", FormActivity.PRIMARY_CARD);
                    startActivity(in);
                }else{
                    Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
                }
            }
        });

        //belum ada kartu sama sekali
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_CARD_SUCCESS, false);
                Intent intent = new Intent(activity, AddCardBarcodeActivity.class);
                startActivity(intent);
            }
        });

        bottomSheet.setVisibility(View.INVISIBLE);

        swipe.setOnTouchListener(new OnSwipeTouchListener(activity){
            @Override
            public void onSwipeBottom() {
                swipe.setRefreshing(false);
                if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                fetchingData();
            }

            @Override
            public void onClick() {
                super.onClick();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);
        //apakah ini route lagi dari set primary card ?
        //klo iya, refresh, klo ga, gausah
        boolean route_from_card = PreferenceManager.getBool(activity, Constant.PREFERENCE_ROUTE_CARD_SUCCESS, false);
        if (route_from_card){
            fetchingData();
        }
    }

    private void getLocalData() {
        String mName = PreferenceManager.getString(activity, Constant.PREFERENCE_USER_NAME, "");
        String mBalance = PreferenceManager.getString(activity, Constant.PREFERENCE_BALANCE, "");
        String mBeans = PreferenceManager.getString(activity, Constant.PREFERENCE_BEAN, "");
        String mGreeting = PreferenceManager.getString(activity, Constant.PREFERENCE_GREETING, "");

        List<CardPrimaryEntity> cardPrimaryEntities = cardController.getCards();
        //ada primary card
        if(cardPrimaryEntities.size() > 0){
            not_set.setVisibility(View.GONE);
            no_cards.setVisibility(View.GONE);
            is_set.setVisibility(View.VISIBLE);
            bottom_sheet_arrow.setVisibility(View.VISIBLE);
            CardPrimaryEntity entity = cardPrimaryEntities.get(0);
            cardName.setText(entity.getName());
            cardBalance.setText(String.valueOf(entity.getBalance()));
            cardBeans.setText(String.valueOf(entity.getPoint()));
            try{
                Glide.with(activity).load(entity.getBarcode()).placeholder(R.drawable.ic_no_image).into(cardImage);
            }catch (Exception e){
                e.printStackTrace();
            }
            //klo ada, tampilkan bottomsheet
        }else{
            //klo ga ada , tapi blm set
            int jumlah = PreferenceManager.getInt(activity, Constant.PREFERENCE_CARD_AMOUNT, 0);
            if(jumlah > 0){
                is_set.setVisibility(View.GONE);
                no_cards.setVisibility(View.GONE);
                not_set.setVisibility(View.VISIBLE);
                bottom_sheet_arrow.setVisibility(View.GONE);
            }else{
                //belum ada sama sekali
                is_set.setVisibility(View.GONE);
                not_set.setVisibility(View.GONE);
                no_cards.setVisibility(View.VISIBLE);
                bottom_sheet_arrow.setVisibility(View.GONE);
            }
        }
        bottomSheet.setVisibility(View.VISIBLE);

        //setGreeting(mDayPart);
        greeting.setText(mGreeting);
        name.setText(mName);
        balance.setText(mBalance.equals("") ? "" : "IDR " + mBalance);
        point.setText(mBeans.equals("") ? "" : mBeans + " Beans");
    }

    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        HomeTask task = new HomeTask(activity) {
            @Override
            public void onSuccess(HomeResponseModel response) {
                //Log.d("HomeResponseModel", response.toString());
                PreferenceManager.putString(activity, Constant.PREFERENCE_USER_NAME, response.getUsername());
                PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(response.getBalance()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(response.getBeans()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_GREETING, response.getSalam());
                PreferenceManager.putString(activity, Constant.PREFERENCE_EMAIL, response.getEmail());
                PreferenceManager.putString(activity, Constant.PREFERENCE_PHONE, response.getPhone());
                PreferenceManager.putInt(activity, Constant.PREFERENCE_CARD_AMOUNT, response.getCardAmount());

                CardItemResponseModel card = null;
                try{
                    card = response.getPrimaryCard();
                    isPrimaryExist = true;
                    //masukin database
                    cardController.clear();
                    CardPrimaryEntity entity = new CardPrimaryEntity();
                    //entity.setId(card.getId_card());
                    entity.setId(1);
                    entity.setName(card.getCard_name());
                    entity.setNumber(card.getCard_number());
                    entity.setImage(card.getCard_image());
                    entity.setDistribution_id(card.getDistribution_id());
                    entity.setCard_pin(card.getCard_pin());
                    entity.setBalance(card.getBalance());
                    entity.setPoint(card.getBeans());
                    entity.setBarcode(card.getBarcode());
                    entity.setExpired_date(card.getExpired_date());
                    entity.setPrimary(card.getPrimary());
                    cardController.insert(entity);
                }catch (Exception e){
                    isPrimaryExist = false;
                    e.printStackTrace();
                }
                if(card == null) isPrimaryExist = false;

                getLocalData();

                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
//                swipe.setRefreshing(false);
                //Toast.makeText(getActivity(), "ON failed", Toast.LENGTH_LONG).show();
                setGreeting(mDayPart);
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute();
    }

    private void setGreeting(int daypart) {
        boolean isLoggedIn = PreferenceManager.getBool(activity, Constant.PREFERENCE_LOGGED_IN, false);
        String hello = "";
        if (daypart == Utils.MORNING) {
            hello = "Good Morning";
        } else if (daypart == Utils.AFTERNOON) {
            hello = "Good Afternoon";
        } else if (daypart == Utils.EVENING) {
            hello = "Good Evening";
        }
        greeting.setText(hello + (isLoggedIn ? "," : ""));
    }

    private void setBackground(int dayPart) {
        int background = R.drawable.bg_morning;
        int navbar = R.drawable.bg_morning_navbar;

        if (dayPart == Utils.MORNING) {
            background = R.drawable.bg_morning;
            navbar = R.drawable.bg_morning_navbar;
        } else if (dayPart == Utils.AFTERNOON) {
            background = R.drawable.bg_afternoon;
            navbar = R.drawable.bg_afternoon_navbar;
        } else if (dayPart == Utils.EVENING) {
            background = R.drawable.bg_evening;
            navbar = R.drawable.bg_evening_navbar;
        }
        activity.setRootBackground(background);
        activity.setNavbarBackground(navbar);
    }

}
