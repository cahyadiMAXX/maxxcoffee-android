package com.maxxcoffee.mobile.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.fragment.dialog.OkDialog;
import com.maxxcoffee.mobile.gcm.GCMRegistrationIntentService;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.task.LoginTestTask;
import com.maxxcoffee.mobile.task.ProfileTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.GpsTracker;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.List;
import java.util.Properties;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.maxxcoffee.mobile.task.LoginTask;
import com.maxxcoffee.mobile.task.OauthTask;
import com.maxxcoffee.mobile.util.WakeLocker;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class LoginFragment extends Fragment {

    private static String[] PERMISSIONS_LOCATION = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};

    private MainActivity activity;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    private ProfileController profileController;
    private CardController cardController;
    private int mDayPart = Utils.getDayPart();

    private boolean settingRequested;
    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);

        profileController = new ProfileController(activity);
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Login", true);

        //setupGCM();

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

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);
        //requestLocationPermission();
    }

    @OnClick(R.id.signup)
    public void onSignUpClick() {
        signUpNow();
    }

    @OnClick(R.id.forgot_password)
    public void onForgotPasswordClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.FORGOT_PASSWORD);

        startActivity(intent);
    }

    @OnClick(R.id.signin)
    public void onSignInClick() {
        loginNow();
    }

    private void signUpNow() {
        PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_FROM_LOGIN, true);
        activity.switchFragment(MainActivity.SIGNUP);
    }

    private void loginNow() {
        if (!isValidForm())
            return;

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        Properties properties = Utils.getProperties(activity);

        final LoginRequestModel loginBody = new LoginRequestModel();
        loginBody.setEmail(email.getText().toString());
        loginBody.setPassword(password.getText().toString());

        /*TelephonyManager mngr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);*/
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        loginBody.setDevice_id(deviceId);
        //loginBody.setDevice_token(PreferenceManager.getString(getActivity(),Constant.PREFERENCE_DEVICE_TOKEN, ""));
        //loginBody.setGadget_id(mngr.getDeviceId());

        //get location here

        final OauthRequestModel oauthBody = new OauthRequestModel();
        oauthBody.setUsername(email.getText().toString());
        oauthBody.setPassword(password.getText().toString());
        oauthBody.setClient_id(properties.getProperty("client_id"));
        oauthBody.setClient_secret(properties.getProperty("client_secret"));
        oauthBody.setGrant_type(properties.getProperty("grant_type"));

        final LoginTask task = new LoginTask(activity) {
            @Override
            public void onSuccess(String status) {
                progress.dismissAllowingStateLoss();

                PreferenceManager.putBool(activity, Constant.PREFERENCE_LOGGED_IN, true);
                PreferenceManager.putString(activity, Constant.PREFERENCE_EMAIL, email.getText().toString());

                fetchingProfileData();
            }

            @Override
            public void onFailed(String status) {
                progress.dismissAllowingStateLoss();

                Toast.makeText(activity, status, Toast.LENGTH_SHORT).show();
            }
        };

        final OauthTask oauthTask = new OauthTask(activity) {
            @Override
            public void onSuccess() {
                task.execute(loginBody);
            }

            @Override
            public void onFailed() {

                progress.dismissAllowingStateLoss();

                //Toast.makeText(activity, "The credentials you entered don't match.", Toast.LENGTH_SHORT).show();
            }
        };

        LoginTestTask loginTestTask = new LoginTestTask(activity) {
            @Override
            public void onSuccess() {
                oauthTask.execute(oauthBody);
            }

            @Override
            public void onChangePhoneNumber(final String email) {

                progress.dismissAllowingStateLoss();

                final Bundle bundle = new Bundle();
                bundle.putString("content", "Phone number already registered. Please change your phone number.");

                OkDialog optionDialog = new OkDialog() {
                    @Override
                    protected void onOk() {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("email", email);
                        bundle1.putString("password", password.getText().toString());

                        Intent intent = new Intent(activity, FormActivity.class);
                        intent.putExtra("content", FormActivity.CHANGE_OLD_PHONE);
                        intent.putExtras(bundle1);

                        startActivity(intent);
                        dismiss();
                    }
                };
                optionDialog.setArguments(bundle);
                optionDialog.show(getFragmentManager(), null);
            }

            @Override
            public void onFailed(String message) {

                progress.dismissAllowingStateLoss();

                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        };
        loginTestTask.execute(loginBody);
    }

    public boolean isValidForm() {
        String mEmail = email.getText().toString();
        String mPassword = password.getText().toString();
        boolean status = true;

        Log.d("lat-long", PreferenceManager.getString(activity, Constant.PREFERENCE_LATITUDE_USER, "") + " " +
        PreferenceManager.getString(activity, Constant.PREFERENCE_LONGITUDE_USER, ""));

        if (mEmail.equals("")) {
            Toast.makeText(activity, "Please verify your email", Toast.LENGTH_SHORT).show();
            email.setError("Please verify your email");
            status = false;
        } else if (mPassword.equals("")) {
            password.setError("Please verify your password");
            Toast.makeText(activity, "Please verify your password", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }

    private void fetchingProfileData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        ProfileTask task = new ProfileTask(activity) {
            @Override
            public void onSuccess(ProfileResponseModel profile) {
                ProfileItemResponseModel profileItem = profile.getUser_profile().get(0);

                if (profileItem != null) {
                    ProfileEntity profileEntity = new ProfileEntity();
                    profileEntity.setId(profileItem.getId_user());
                    profileEntity.setName(profileItem.getNama_user());
                    profileEntity.setEmail(profileItem.getEmail());
                    profileEntity.setCity(profileItem.getKota_user());
                    profileEntity.setPhone(profileItem.getMobile_phone_user());
                    profileEntity.setBirthday(profileItem.getTanggal_lahir());
                    profileEntity.setGender(profileItem.getGender());
                    profileEntity.setOccupation(profileItem.getOccupation());
                    profileEntity.setImage(profileItem.getGambar());
                    profileEntity.setPoint(Integer.parseInt(profile.getTotal_point()));
                    profileEntity.setBalance(Integer.parseInt(profile.getTotal_balance()));
                    profileEntity.setSms_verified(profileItem.getVerifikasi_sms().equalsIgnoreCase("yes"));
                    profileEntity.setEmail_verified(profileItem.getVerifikasi_email().equalsIgnoreCase("yes"));

                    PreferenceManager.putBool(activity, Constant.PREFERENCE_SMS_VERIFICATION, profileItem.getVerifikasi_sms().equalsIgnoreCase("yes"));
                    PreferenceManager.putBool(activity, Constant.PREFERENCE_EMAIL_VERIFICATION, profileItem.getVerifikasi_email().equalsIgnoreCase("yes"));

                    profileController.insert(profileEntity);

                    List<CardItemResponseModel> cards = profile.getCards();
                    if (cards.size() > 0) {

                        for (CardItemResponseModel card : cards) {
                            CardEntity cardEntity = new CardEntity();
                            cardEntity.setId(card.getId_card());
                            cardEntity.setName(card.getCard_name());
                            cardEntity.setNumber(card.getCard_number());
                            cardEntity.setImage(card.getCard_image());
                            cardEntity.setDistribution_id(card.getDistribution_id());
                            cardEntity.setCard_pin(card.getCard_pin());
                            cardEntity.setBalance(card.getBalance());
                            cardEntity.setPoint(card.getBeans());
                            cardEntity.setExpired_date(card.getExpired_date());

                            cardController.insert(cardEntity);
                        }
                    }
                }
                PreferenceManager.putBool(activity, Constant.PREFERENCE_WELCOME_SKIP, true);
                PreferenceManager.putBool(activity, Constant.PREFERENCE_TUTORIAL_SKIP, true);
                PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(profile.getTotal_balance()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(profile.getTotal_point()));

                progress.dismissAllowingStateLoss();

                activity.prepareDrawerList();
                activity.switchFragment(MainActivity.HOME);
            }

            @Override
            public void onFailed() {

                progress.dismiss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void setBackground(int dayPart) {
        int navbar = R.drawable.bg_morning_navbar;

        if (dayPart == Utils.MORNING) {
            navbar = R.drawable.bg_morning_navbar;
        } else if (dayPart == Utils.AFTERNOON) {
            navbar = R.drawable.bg_afternoon_navbar;
        } else if (dayPart == Utils.EVENING) {
            navbar = R.drawable.bg_evening_navbar;
        }
        activity.setRootBackground(navbar);
        activity.setNavbarBackground(navbar);
    }
}
