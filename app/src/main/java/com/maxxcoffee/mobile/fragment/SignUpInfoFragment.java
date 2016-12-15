package com.maxxcoffee.mobile.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.controller.CardController;
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.fragment.dialog.BirthdateDialog;
import com.maxxcoffee.mobile.fragment.dialog.GenderDialog;
import com.maxxcoffee.mobile.fragment.dialog.List2Dialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileItemResponseModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.task.ProfileTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.maxxcoffee.mobile.task.LoginTask;
import com.maxxcoffee.mobile.task.OauthTask;
import com.maxxcoffee.mobile.task.RegisterTask;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class SignUpInfoFragment extends Fragment {

    private final int MALE = 999;
    private final int FEMALE = 777;

    @Bind(R.id.gender)
    TextView textGender;
    @Bind(R.id.birthday)
    TextView textBirthday;
    @Bind(R.id.city)
    TextView textCity;
    @Bind(R.id.occupation)
    TextView textOccupation;
    @Bind(R.id.referal_code)
    EditText fieldReferalCode;

    private MainActivity activity;
    private SimpleDateFormat dateFormat;
    private ProfileController profileController;
    private CardController cardController;
    private int selectedGender;
    private String selectedDate;
    private String selectedFirstName;
    private String selectedLastName;
    private String selectedEmail;
    private String selectedPhoneNumber;
    private String selectedPassword;
    private int mDayPart = Utils.getDayPart();

    public SignUpInfoFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        profileController = new ProfileController(activity);
        cardController = new CardController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_info, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Sign Up", true);

        selectedFirstName = getArguments().getString("first-name");
        selectedLastName = getArguments().getString("last-name");
        selectedEmail = getArguments().getString("email");
        selectedPhoneNumber = getArguments().getString("phone");
        selectedPassword = getArguments().getString("password");


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);

        String mGender = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_GENDER, "");
        String mBirthday = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_BIRTHDAY, "");
        String mOccupation = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_OCCUPATION, "");
        String mCity = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_CITY, "");

        textGender.setText(mGender);
        textBirthday.setText(mBirthday);
        textOccupation.setText(mOccupation);
        textCity.setText(mCity);

        if (mGender.equalsIgnoreCase("Male")) {
            selectedGender = MALE;
        } else if (mGender.equalsIgnoreCase("Female")) {
            selectedGender = FEMALE;
        }
    }

    @OnClick(R.id.gender_layout)
    public void onGenderClick() {
        GenderDialog reportDialog = new GenderDialog() {
            @Override
            protected void onOk(Integer gender) {
                selectedGender = gender;
                if (selectedGender == MALE) {
                    textGender.setText("Male");
                } else if (selectedGender == FEMALE) {
                    textGender.setText("Female");
                }
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_GENDER, textGender.getText().toString());
                dismiss();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt("selected-gender", selectedGender);

        reportDialog.setArguments(bundle);
        reportDialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.occupation_layout)
    public void onOccupationClick() {
        List<String> occupations = Arrays.asList(Constant.OCCUPATION_LIST);
        String jsonOccupations = new Gson().toJson(occupations);

        List2Dialog reportDialog = new List2Dialog() {
            @Override
            public void onSelectedItem(String item) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_OCCUPATION, item);
                textOccupation.setText(item);
                dismiss();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putString("title", "Occupation");
        bundle.putString("data", jsonOccupations);

        reportDialog.setArguments(bundle);
        reportDialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.city_layout)
    public void onCityClick() {
        String jsonCity = PreferenceManager.getString(activity, Constant.DATA_KOTA, "");

        List2Dialog reportDialog = new List2Dialog() {
            @Override
            public void onSelectedItem(String item) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_CITY, item);
                textCity.setText(item);
                dismiss();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putString("title", "City");
        bundle.putString("data", jsonCity);

        reportDialog.setArguments(bundle);
        reportDialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.birthday_layout)
    public void onBirthdayClick() {

        BirthdateDialog datePicker = new BirthdateDialog(activity, BirthdateDialog.DATE_VALIDARION_OFF) {
            @Override
            protected void onDateSelected(Date date) {
                if (date != null) {
                    selectedDate = dateFormat.format(date);
                    textBirthday.setText(selectedDate);
                    PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_BIRTHDAY, selectedDate);
                }
                dismiss();
            }

            @Override
            protected void onError(String message) {
                if (!message.equalsIgnoreCase("")) {
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                }
            }
        };
        datePicker.show(getFragmentManager(), null);
    }

    @OnClick(R.id.signup)
    public void onSignUpClick() {
        if (!isFormValid())
            return;

        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/

        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        RegisterRequestModel body = new RegisterRequestModel();
        body.setFirst_name(selectedFirstName);
        body.setLast_name(selectedLastName);
        body.setEmail(selectedEmail);
        body.setPassword(selectedPassword);
        body.setKota_user(textCity.getText().toString());
        body.setMobile_phone_user(selectedPhoneNumber);
        body.setGender(textGender.getText().toString());
        body.setOccupation(textOccupation.getText().toString());
        body.setTanggal_lahir(textBirthday.getText().toString());
        body.setReferral_code(fieldReferalCode.getText().toString());

        /*TelephonyManager mngr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);*/
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        body.setDevice_id(deviceId);
        //body.setGadget_id(mngr.getDeviceId());

        RegisterTask task = new RegisterTask(activity) {
            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                loginNow();
            }

            @Override
            public void onFailed(String message) {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    private void loginNow() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        Properties properties = Utils.getProperties(activity);

        final LoginRequestModel loginBody = new LoginRequestModel();
        loginBody.setEmail(selectedEmail);
        loginBody.setPassword(selectedPassword);

        //get location here

        final OauthRequestModel oauthBody = new OauthRequestModel();
        oauthBody.setUsername(selectedEmail);
        oauthBody.setPassword(selectedPassword);
        oauthBody.setClient_id(properties.getProperty("client_id"));
        oauthBody.setClient_secret(properties.getProperty("client_secret"));
        oauthBody.setGrant_type(properties.getProperty("grant_type"));

        final LoginTask task = new LoginTask(activity) {
            @Override
            public void onSuccess(String status) {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                PreferenceManager.putBool(activity, Constant.PREFERENCE_LOGGED_IN, true);
                fetchingProfileData();
            }

            @Override
            public void onFailed(String status) {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, "Login failed. Token not found", Toast.LENGTH_SHORT).show();
            }
        };

        OauthTask oauthTask = new OauthTask(activity) {
            @Override
            public void onSuccess() {
                task.execute(loginBody);
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, "Login failed. Access token not found", Toast.LENGTH_SHORT).show();
            }
        };
        oauthTask.execute(oauthBody);
    }

    private boolean isFormValid() {
        String mGender = textGender.getText().toString();
        String mBirthday = textBirthday.getText().toString();
        String mOccupation = textOccupation.getText().toString();
        String mCity = textCity.getText().toString();

        if (mGender.equals("")) {
            textGender.setError("Please verify your gender");
            Toast.makeText(activity, "Please verify your gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBirthday.equals("")) {
            textBirthday.setError("Please verify your birthday");
            Toast.makeText(activity, "Please verify your birthday", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mOccupation.equals("")) {
            textOccupation.setError("Please verify your occupation");
            Toast.makeText(activity, "Please verify your occupation", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mCity.equals("")) {
            textCity.setError("Please verify your city");
            Toast.makeText(activity, "Please verify your city", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void fetchingProfileData() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

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
                //progress.dismissAllowingStateLoss();
                loading.dismiss();

                activity.prepareDrawerList();
                activity.switchFragment(MainActivity.HOME);
                clearTemporaryData();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void clearTemporaryData() {
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_FIRST_NAME);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_LAST_NAME);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_GENDER);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_BIRTHDAY);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_OCCUPATION);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_CITY);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_EMAIL);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_PASSWORD);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_PHONE);
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
