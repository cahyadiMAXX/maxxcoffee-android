package com.maxxcoffee.mobile.ui.privacy;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.FormActivity;
import com.maxxcoffee.mobile.task.extra.PrivacyTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class PrivacySignUpFragment extends Fragment {

    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.content)
    TextView content;

    private FormActivity activity;

    public PrivacySignUpFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tos, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Privacy Policy");

        content.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_PRIVACY, ""));
        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void fetchingData() {
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

        PrivacyTask task = new PrivacyTask(activity) {
            @Override
            public void onSuccess(String tos) {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();

                if (tos != null) {
                    content.setText(Html.fromHtml(tos));
                    PreferenceManager.putString(activity, Constant.PREFERENCE_PRIVACY, tos);
                } else {
                    content.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_PRIVACY, ""));
                }

            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }
}
