package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rioswarawan on 7/19/16.
 */
public class RenameCardFragment extends Fragment {

    @Bind(R.id.name)
    EditText name;

    private FormActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rename_card, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Rename Card");

        return view;
    }

    @OnClick(R.id.done)
    public void onDoneClick() {

    }
}
