package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ProfileFragment extends Fragment {

    private MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Profile");

        return view;
    }

    @OnClick(R.id.layout_name)
    public void onNameClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_NAME);
        startActivity(intent);
    }

    @OnClick(R.id.layout_email)
    public void onEmailClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_EMAIL);
        startActivity(intent);
    }

    @OnClick(R.id.layout_change_password)
    public void onChangePasswordClick() {
        Intent intent = new Intent(activity, FormActivity.class);
        intent.putExtra("content", FormActivity.CHANGE_PASSWORD);
        startActivity(intent);
    }
}
