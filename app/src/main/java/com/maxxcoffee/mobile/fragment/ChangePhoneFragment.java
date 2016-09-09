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
import com.maxxcoffee.mobile.database.controller.ProfileController;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.request.ChangePhoneRequestModel;
import com.maxxcoffee.mobile.task.ChangePhoneTask;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangePhoneFragment extends Fragment {

    @Bind(R.id.phone)
    EditText phone;

    private FormActivity activity;
    private ProfileController profileController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
        profileController = new ProfileController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_phone, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Change Phone Number");

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

        final ProfileEntity profile = profileController.getProfile();
        if (profile != null) {
            final LoadingDialog progress = new LoadingDialog();
            progress.show(getFragmentManager(), null);

            ChangePhoneRequestModel body = new ChangePhoneRequestModel();
            body.setNewhp(phone.getText().toString());
            body.setEmail(profile.getEmail());

            ChangePhoneTask task = new ChangePhoneTask(activity) {
                @Override
                public void onSuccess() {

                    progress.dismissAllowingStateLoss();
                    profile.setPhone(phone.getText().toString());
                    profileController.insert(profile);

                    activity.onBackClick();
                }

                @Override
                public void onWait() {

                    progress.dismissAllowingStateLoss();
                    Toast.makeText(activity, "You've just changed your phone number. Please wait 5 minutes before making another changes.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed() {

                    progress.dismiss();
                    Toast.makeText(activity, "Failed to change phone number", Toast.LENGTH_SHORT).show();
                }
            };
            task.execute(body);
        }
    }

    private boolean isFormValid() {
        String mPhone = phone.getText().toString();
        boolean status = true;

        if (mPhone.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your phone number.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }
}
