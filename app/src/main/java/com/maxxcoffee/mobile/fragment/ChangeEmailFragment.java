package com.maxxcoffee.mobile.fragment;

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
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.fragment.dialog.ChangeEmailDialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.request.ChangeEmailRequestModel;
import com.maxxcoffee.mobile.task.ChangeEmailTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangeEmailFragment extends Fragment {

    final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    private FormActivity activity;

    public ChangeEmailFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Change Email");

        return view;
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        if (!isFormValid())
            return;

        ChangeEmailDialog dialog = new ChangeEmailDialog() {

            @Override
            protected void onChangeEmail() {
                changeEmailNow();
                dismiss();
            }
        };
        dialog.show(getFragmentManager(), null);
    }

    private void changeEmailNow() {
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

        ChangeEmailRequestModel body = new ChangeEmailRequestModel();
        body.setNew_email(email.getText().toString());
        body.setPassword(password.getText().toString());

        ChangeEmailTask task = new ChangeEmailTask(activity) {
            @Override
            public void onSuccess(String message) {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                PreferenceManager.putBool(activity, Constant.PREFERENCE_ROUTE_TO_LOGOUT, true);
                activity.onBackClick();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                loading.dismiss();
                Toast.makeText(activity, "Failed to change email. Contact administrator for more information.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    private boolean isFormValid() {
        if (email.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill email form", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!emailPattern.matcher(email.getText().toString()).matches()) {
            Toast.makeText(activity, "Email not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
