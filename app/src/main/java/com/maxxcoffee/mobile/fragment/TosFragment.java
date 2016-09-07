package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.task.TosTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class TosFragment extends Fragment {

    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.content)
    TextView content;

    private MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tos, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Term of Service");

        content.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_TOS, ""));
        if(Utils.isConnected(activity)){
            fetchingData();
        }else{
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        TosTask task = new TosTask(activity) {
            @Override
            public void onSuccess(String tos) {
                progress.dismissAllowingStateLoss();

                if (tos != null) {
                    content.setText(Html.fromHtml(tos));
                    PreferenceManager.putString(activity, Constant.PREFERENCE_TOS, tos);
                } else {
                    content.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_TOS, ""));
                }

            }

            @Override
            public void onFailed() {
                progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }
}
