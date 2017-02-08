package com.maxxcoffee.mobile.ui.login;

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
import com.maxxcoffee.mobile.task.user.ForgotPasswordTask;
import com.maxxcoffee.mobile.util.Utils;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ForgotPasswordFragment extends Fragment {

    final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Bind(R.id.email)
    EditText email;

    private FormActivity activity;

    public ForgotPasswordFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Forgot Password");

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
        body.setEmail(email.getText().toString());

        ForgotPasswordTask task = new ForgotPasswordTask(activity) {
            @Override
            public void onSuccess() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, "Verification link has been sent to Your email. Please verify your email address.", Toast.LENGTH_LONG).show();
                activity.onBackClick();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String message) {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        };
        task.execute(body);
    }

    private boolean isFormValid() {
        if (email.getText().toString().equals("")) {
            Toast.makeText(activity, "Please fill email form", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!emailPattern.matcher(email.getText().toString()).matches()) {
            Toast.makeText(activity, "Email not valid", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
