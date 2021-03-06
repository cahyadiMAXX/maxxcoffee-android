package com.maxxcoffee.mobile.ui.profile;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.FormActivity;
import com.maxxcoffee.mobile.model.request.ChangePhoneRequestModel;
import com.maxxcoffee.mobile.task.profile.ChangeOldPhoneTask;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangeOldPhoneFragment extends Fragment {

    @Bind(R.id.phone)
    EditText phone;

    private FormActivity activity;

    public ChangeOldPhoneFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
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

        String email = getArguments().getString("email");
        String password = getArguments().getString("password");

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

        ChangePhoneRequestModel body = new ChangePhoneRequestModel();
        body.setMobile_phone_user(phone.getText().toString());
        body.setEmail(email);
        body.setPassword(password);

        ChangeOldPhoneTask task = new ChangeOldPhoneTask(activity) {
            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                activity.onBackClick();
            }

            @Override
            public void onWait() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, "You've just changed your phone number. Please wait 5 minutes before making another changes.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                //progress.dismiss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, "Failed to change phone number", Toast.LENGTH_LONG).show();
            }
        };
        task.execute(body);
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
