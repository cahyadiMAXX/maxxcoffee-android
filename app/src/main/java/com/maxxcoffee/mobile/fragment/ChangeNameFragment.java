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
import com.maxxcoffee.mobile.widget.TBaseProgress;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.maxxcoffee.mobile.task.ChangeNameTask;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangeNameFragment extends Fragment {

    @Bind(R.id.name)
    EditText name;

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

        return view;
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        if (!isFormValid())
            return;

        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        String mName = name.getText().toString();
        ChangeNameTask task = new ChangeNameTask(activity) {
            @Override
            public void onSuccess() {
                if (progress.isShowing())
                    progress.dismiss();
                activity.onBackClick();
            }

            @Override
            public void onFailed() {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity, "Failed to change user name", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(mName);
    }

    private boolean isFormValid() {
        String mName = name.getText().toString();
        boolean status = true;

        if (mName.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your name.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }
}
