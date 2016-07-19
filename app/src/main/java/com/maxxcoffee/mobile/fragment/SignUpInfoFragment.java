package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.fragment.dialog.BirthdateDialog;
import com.maxxcoffee.mobile.fragment.dialog.GenderDialog;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterRequestModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    private MainActivity activity;
    private SimpleDateFormat dateFormat;
    private int selectedGender;
    private String selectedDate;
    private String selectedName;
    private String selectedEmail;
    private String selectedPhoneNumber;
    private String selectedPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        activity.setRootBackground(R.drawable.bg_navbar);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_info, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("");

        selectedName = getArguments().getString("name");
        selectedEmail = getArguments().getString("email");
        selectedPhoneNumber = getArguments().getString("phone");
        selectedPassword = getArguments().getString("password");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

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
                    textGender.setText("Male");
                }
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

    @OnClick(R.id.birthday_layout)
    public void onBirthdayClick() {
        BirthdateDialog datePicker = new BirthdateDialog(activity, BirthdateDialog.DATE_VALIDARION_OFF) {
            @Override
            protected void onDateSelected(Date date) {
                if (date != null) {
                    selectedDate = dateFormat.format(date);
                    textBirthday.setText(selectedDate);
                }
                dismiss();
            }

            @Override
            protected void onError(String message) {
            }
        };
        datePicker.show(getFragmentManager(), null);
    }

    @OnClick(R.id.signup)
    public void onSignUpClick() {
        if (!isFormValid())
            return;

        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        RegisterRequestModel body = new RegisterRequestModel();
        body.setNama_user(selectedName);
        body.setEmail(selectedEmail);
        body.setPassword(selectedPassword);
        body.setKota_user(textCity.getText().toString());
        body.setMobile_phone_user(selectedPhoneNumber);
        body.setGender(textGender.getText().toString());
        body.setOccupation(textOccupation.getText().toString());
        body.setTanggal_lahir(selectedDate);

        RegisterTask task = new RegisterTask(activity) {
            @Override
            public void onSuccess() {
                if (progress.isShowing())
                    progress.dismiss();
                loginNow();
            }

            @Override
            public void onFailed(String message) {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity, "Register failed. " + message, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    private void loginNow() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        Properties properties = Utils.getProperties(activity);

        final LoginRequestModel loginBody = new LoginRequestModel();
        loginBody.setEmail(selectedEmail);
        loginBody.setPassword(selectedPassword);

        final OauthRequestModel oauthBody = new OauthRequestModel();
        oauthBody.setUsername(selectedEmail);
        oauthBody.setPassword(selectedPassword);
        oauthBody.setClient_id(properties.getProperty("client_id"));
        oauthBody.setClient_secret(properties.getProperty("client_secret"));
        oauthBody.setGrant_type(properties.getProperty("grant_type"));

        final LoginTask task = new LoginTask(activity) {
            @Override
            public void onSuccess() {
                if (progress.isShowing())
                    progress.dismiss();

                PreferenceManager.putBool(activity, Constant.PREFERENCE_LOGGED_IN, true);
                activity.prepareDrawerList();
                activity.switchFragment(MainActivity.HOME);
                clearTemporaryData();
            }

            @Override
            public void onFailed() {
                if (progress.isShowing())
                    progress.dismiss();

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
                if (progress.isShowing())
                    progress.dismiss();

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
            Toast.makeText(activity, "Please verify your gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mBirthday.equals("")) {
            Toast.makeText(activity, "Please verify your birthday", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mOccupation.equals("")) {
            Toast.makeText(activity, "Please verify your occupation", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mCity.equals("")) {
            Toast.makeText(activity, "Please verify your city", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearTemporaryData() {
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_NAME);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_GENDER);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_BIRTHDAY);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_OCCUPATION);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_CITY);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_EMAIL);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_PASSWORD);
        PreferenceManager.remove(activity, Constant.PREFERENCE_REGISTER_PHONE);
    }
}
