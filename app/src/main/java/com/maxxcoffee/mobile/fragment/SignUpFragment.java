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
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class SignUpFragment extends Fragment {

    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.password_confirm)
    EditText passwordConfirm;

    private MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        activity.setRootBackground(R.drawable.bg_navbar);
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

        String mName = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_NAME, "");
        String mEmail = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_EMAIL, "");
        String mPhone = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_PHONE, "");
        String mPassword = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_PASSWORD, "");

        name.setText(mName);
        email.setText(mEmail);
        phone.setText(mPhone);
        password.setText(mPassword);
    }

    @OnClick(R.id.next)
    public void onNextClick() {
        if (!isFormValid())
            return;

        String mName = name.getText().toString();
        String mEmail = email.getText().toString();
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("name", mName);
        bundle.putString("email", mEmail);
        bundle.putString("phone", mPhone);
        bundle.putString("password", mPassword);

        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_NAME, mName);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_EMAIL, mEmail);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PHONE, mPhone);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PASSWORD, mPassword);

        activity.switchFragment(MainActivity.SIGNUP_INFO, bundle);
    }

    private boolean isFormValid() {
        String mName = name.getText().toString();
        String mEmail = email.getText().toString();
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();
        String mPasswordConfirm = passwordConfirm.getText().toString();

        if (mName.equals("")) {
            Toast.makeText(activity, "Please verify your name", Toast.LENGTH_SHORT).show();
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
}
