package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.task.CityTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class SignUpFragment extends Fragment {

    @Bind(R.id.first_name)
    EditText firstName;
    @Bind(R.id.last_name)
    EditText lastName;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.password_confirm)
    EditText passwordConfirm;

    private MainActivity activity;
    private int mDayPart = Utils.getDayPart();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);

        String mFirstName = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_FIRST_NAME, "");
        String mLastName = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_LAST_NAME, "");
        String mEmail = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_EMAIL, "");
        String mPhone = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_PHONE, "");
        String mPassword = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_PASSWORD, "");

        firstName.setText(mFirstName);
        lastName.setText(mLastName);
        email.setText(mEmail);
        phone.setText(mPhone);
        password.setText(mPassword);
    }

    @OnClick(R.id.next)
    public void onNextClick() {
        if (!isFormValid())
            return;

        String mFirstName = firstName.getText().toString();
        String mLastName = lastName.getText().toString();
        String mEmail = email.getText().toString();
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();

        final Bundle bundle = new Bundle();
        bundle.putString("first-name", mFirstName);
        bundle.putString("last-name", mLastName);
        bundle.putString("email", mEmail);
        bundle.putString("phone", mPhone);
        bundle.putString("password", mPassword);

        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_FIRST_NAME, mFirstName);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_LAST_NAME, mLastName);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_EMAIL, mEmail);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PHONE, mPhone);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PASSWORD, mPassword);

//        String cityData = PreferenceManager.getString(activity, Constant.DATA_KOTA, "");

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        CityTask task = new CityTask(activity) {
            @Override
            public void onSuccess(String json) {
                progress.dismissAllowingStateLoss();
                PreferenceManager.putString(activity, Constant.DATA_KOTA, json);
                activity.switchFragment(MainActivity.SIGNUP_INFO, bundle);
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Failed to retrieve city data", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private boolean isFormValid() {
        String mFirstName = firstName.getText().toString();
        String mLastName = lastName.getText().toString();
        String mEmail = email.getText().toString();
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();
        String mPasswordConfirm = passwordConfirm.getText().toString();

        if (mFirstName.equals("")) {
            Toast.makeText(activity, "Please verify your first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mLastName.equals("")) {
            Toast.makeText(activity, "Please verify your last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEmail.equals("")) {
            Toast.makeText(activity, "Please verify your email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mPhone.equals("")) {
            Toast.makeText(activity, "Please verify your phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mPassword.equals("")) {
            Toast.makeText(activity, "Please verify your password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mPasswordConfirm.equals("")) {
            Toast.makeText(activity, "Please verify your password confirmation", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mPassword.equals(mPasswordConfirm)) {
            Toast.makeText(activity, "Please verify your password confirmation", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
