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
import com.maxxcoffee.mobile.task.AboutTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class AboutFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("About");

        content.setText(Html.fromHtml(PreferenceManager.getString(activity, Constant.PREFERENCE_ABOUT, "")));
        fetchingData();
        return view;
    }

    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        AboutTask task = new AboutTask(activity) {
            @Override
            public void onSuccess(String about) {
                    progress.dismissAllowingStateLoss();

                if (about != null) {
                    content.setText(Html.fromHtml(about));
                    PreferenceManager.putString(activity, Constant.PREFERENCE_ABOUT, about);
                } else {
                    content.setText(PreferenceManager.getString(activity, Constant.PREFERENCE_ABOUT, ""));
                }
            }

            @Override
            public void onFailed() {
                    progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }
}
