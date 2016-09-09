package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.maxxcoffee.mobile.task.ChangeNameTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangeNameFragment extends Fragment {

    @Bind(R.id.first_name)
    EditText firstName;
    @Bind(R.id.last_name)
    EditText lastName;

    private FormActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_name, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Change Name");

        firstName.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_FIRST_NAME, ""));
        lastName.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_LAST_NAME, ""));

        return view;
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        if (!isFormValid())
            return;

        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        String mFirstName = firstName.getText().toString();
        String mLastName = lastName.getText().toString();

        ChangeNameTask task = new ChangeNameTask(activity) {
            @Override
            public void onSuccess() {
                progress.dismissAllowingStateLoss();
                activity.onBackClick();
            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(mFirstName, mLastName);
    }

    private boolean isFormValid() {
        String mFirstName = firstName.getText().toString();
        String mLastName = lastName.getText().toString();
        boolean status = true;

        if (mFirstName.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your first name.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        if (mLastName.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your last name.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }
}
