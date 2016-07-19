package com.maxxcoffee.mobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.util.Properties;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.maxxcoffee.mobile.task.LoginTask;
import com.maxxcoffee.mobile.task.OauthTask;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class LoginFragment extends Fragment {

    private MainActivity activity;

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        activity.setRootBackground(R.drawable.bg_navbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("");

        return view;
    }

    @OnClick(R.id.signup)
    public void onSignUpClick() {
        PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_FROM_LOGIN, true);
        activity.switchFragment(MainActivity.SIGNUP);
    }

    @OnClick(R.id.signin)
    public void onSignInClick() {
        if (!isValidForm())
            return;

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        Properties properties = Utils.getProperties(activity);

        final LoginRequestModel loginBody = new LoginRequestModel();
        loginBody.setEmail(email.getText().toString());
        loginBody.setPassword(password.getText().toString());

        final OauthRequestModel oauthBody = new OauthRequestModel();
        oauthBody.setUsername(email.getText().toString());
        oauthBody.setPassword(password.getText().toString());
        oauthBody.setClient_id(properties.getProperty("client_id"));
        oauthBody.setClient_secret(properties.getProperty("client_secret"));
        oauthBody.setGrant_type(properties.getProperty("grant_type"));

        final LoginTask task = new LoginTask(activity) {
            @Override
            public void onSuccess() {
                if (progress.isShowing())
                    progress.dismiss();

                PreferenceManager.putBool(activity, Constant.PREFERENCE_LOGGED_IN, true);
                PreferenceManager.putString(activity, Constant.PREFERENCE_EMAIL, email.getText().toString());

                activity.prepareDrawerList();
                activity.switchFragment(MainActivity.HOME);
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

    public boolean isValidForm() {
        String mEmail = email.getText().toString();
        String mPassword = password.getText().toString();
        boolean status = true;

        if (mEmail.equals("")) {
            Toast.makeText(activity, "Please verify your email", Toast.LENGTH_SHORT).show();
            status = false;
        } else if (mPassword.equals("")) {
            Toast.makeText(activity, "Please verify your password", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }

}
