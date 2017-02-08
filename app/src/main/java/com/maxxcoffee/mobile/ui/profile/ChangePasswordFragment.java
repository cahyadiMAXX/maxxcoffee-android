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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.maxxcoffee.mobile.task.profile.ChangePasswordTask;
import com.maxxcoffee.mobile.util.Utils;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangePasswordFragment extends Fragment {

    @Bind(R.id.old_password)
    EditText oldPassword;
    @Bind(R.id.new_password)
    EditText newPassword;
    @Bind(R.id.new_password_confirm)
    EditText newPasswordConfirm;

    private FormActivity activity;

    public ChangePasswordFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Change Password");

        return view;
    }


    @OnClick(R.id.save)
    public void onSaveChangePasswordClick() {
        if (!isFormValid())
            return;

        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            return;
        }

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

        String mOldPassword = oldPassword.getText().toString();
        String mNewPassword = newPassword.getText().toString();
        ChangePasswordTask task = new ChangePasswordTask(activity) {
            @Override
            public void onSuccess() {

                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                activity.onBackClick();
            }

            @Override
            public void onFailed() {

                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, "Failed to change password", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(mOldPassword, mNewPassword);
    }

    public boolean isFormValid() {
        String mOldPassword = oldPassword.getText().toString();
        String mNewPassword = newPassword.getText().toString();
        String mNewPasswordConfirm = newPasswordConfirm.getText().toString();
        boolean status = true;

        if (mOldPassword.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your old password.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        if (mNewPassword.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your new password.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        if (mNewPasswordConfirm.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your new password confirmation.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        if (!mNewPassword.equalsIgnoreCase(mNewPasswordConfirm)) {
            Toast.makeText(activity, "Your new password confirmation does not match.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }
}
