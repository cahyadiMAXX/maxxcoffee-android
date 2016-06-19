package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class LoginFragment extends Fragment {

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

}
