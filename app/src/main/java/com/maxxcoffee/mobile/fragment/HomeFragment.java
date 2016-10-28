package com.maxxcoffee.mobile.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.AddCardBarcodeActivity;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.activity.VerificationActivity;
import com.maxxcoffee.mobile.database.controller.CardPrimaryController;
import com.maxxcoffee.mobile.database.entity.CardPrimaryEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.RateAppDialog;
import com.maxxcoffee.mobile.gcm.GCMRegistrationIntentService;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.HomeResponseModel;
import com.maxxcoffee.mobile.task.HomeTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.OnSwipeTouchListener;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.util.WakeLocker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public HomeFragment(){}

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

        boolean isLogoutNow = PreferenceManager.getBool(activity, Constant.PREFERENCE_LOGOUT_NOW, false);
        if(isLogoutNow){
            //activity.logoutNow();
        }

        setupGCM();

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
                boolean isSmsVerified = PreferenceManager.getBool(activity, Constant.PREFERENCE_SMS_VERIFICATION, false);
                boolean isEmailVerified = PreferenceManager.getBool(activity, Constant.PREFERENCE_EMAIL_VERIFICATION, false);

                if (isSmsVerified && isEmailVerified) {
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_CARD_SUCCESS, false);
                    Intent intent = new Intent(activity, AddCardBarcodeActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(activity, VerificationActivity.class);
                    intent.putExtra("redirect-fragment", MainActivity.HOME);

                    startActivity(intent);
                }

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

    void setupGCM(){
        //gcm bos
        //initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    WakeLocker.acquire(getActivity());
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    Log.d("gcmtoken", token);

                    //if the intent is not with success then displaying error messages
                    WakeLocker.release();

                    // Explicitly specify that GcmIntentService will handle the intent.
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getActivity(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getActivity(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getActivity());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getActivity(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            //Toast.makeText(getApplicationContext(), "start register", Toast.LENGTH_LONG).show();
            Intent itent = new Intent(getActivity(), GCMRegistrationIntentService.class);
            getActivity().startService(itent);
        }
    }

    void forceUserRating(){
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
        Date today = new Date();
        final String strToday = df.format(today);
        String date_firstLaunch = PreferenceManager.getString(getActivity(), Constant.PREFERENCE_DATE_FIRST_LAUNCH, strToday);
        Log.d("first_launch", date_firstLaunch);
        Date inputDate = null;
        try {
            inputDate = df.parse(date_firstLaunch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //jangan lupa ganti ke hari
        if (Utils.getDurationInMinutes(inputDate) > (5 * 24 * 60)) {
            final RateAppDialog dialog = new RateAppDialog(){
                @Override
                protected void onOk() {
                    super.onOk();
                    //buka playstore
                    dismiss();
                    PreferenceManager.putBool(getActivity(), Constant.PREFERENCE_HAS_RATED, true);
                    final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }

                @Override
                public void onLaterClick() {
                    super.onLaterClick();
                    //update tanggal
                    PreferenceManager.putString(getActivity(), Constant.PREFERENCE_DATE_FIRST_LAUNCH, strToday);
                    dismiss();
                }

                @Override
                public void onNoClick() {
                    super.onNoClick();
                    //jangan muncul lagi
                    PreferenceManager.putBool(getActivity(), Constant.PREFERENCE_SHOW_AGAIN, false);
                    dismiss();
                }
            };

            dialog.show(getFragmentManager(), null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);
        //apakah ini route lagi dari set prime card ?
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
        //ada prime card
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
        activity.prepareDrawerList();
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
                PreferenceManager.putInt(activity, Constant.PREFERENCE_HAS_VIRTUAL_CARD, response.getVirtual_card());
                PreferenceManager.putBool(activity, Constant.PREFERENCE_SMS_VERIFICATION, response.getVerifikasi_sms().equalsIgnoreCase("yes"));
                PreferenceManager.putBool(activity, Constant.PREFERENCE_EMAIL_VERIFICATION, response.getVerifikasi_email().equalsIgnoreCase("yes"));

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

                //rate aplikasi
                boolean is_rated = PreferenceManager.getBool(getActivity(), Constant.PREFERENCE_HAS_RATED, false);
                boolean show_again= PreferenceManager.getBool(getActivity(), Constant.PREFERENCE_SHOW_AGAIN, true);

                Log.d("is_rated", is_rated ? "rated":"not rated");
                Log.d("show_again", show_again ? "show_again":"not show_again");
                if(!is_rated && show_again){
                    forceUserRating();
                }

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
