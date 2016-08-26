package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.fragment.dialog.List2Dialog;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.request.ChangeCityOccupationRequestModel;
import com.maxxcoffee.mobile.task.ChangeCityOccupationTask;
import com.maxxcoffee.mobile.task.CityTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class ChangeCityFragment extends Fragment {

    @Bind(R.id.city)
    TextView city;

    private FormActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_city, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Change City");

        fetchingCity();
        return view;
    }

    private void fetchingCity() {
        String cityData = PreferenceManager.getString(activity, Constant.DATA_KOTA, "");
        if (cityData.equals("")) {
            final LoadingDialog progress = new LoadingDialog();
            progress.show(getFragmentManager(), null);

            CityTask task = new CityTask(activity) {
                @Override
                public void onSuccess(String json) {
                        progress.dismissAllowingStateLoss();
                    PreferenceManager.putString(activity, Constant.DATA_KOTA, json);
                }

                @Override
                public void onFailed() {
                        progress.dismissAllowingStateLoss();
                    Toast.makeText(activity, "Failed to retrieve city data", Toast.LENGTH_SHORT).show();
                }
            };
            task.execute();
        }
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        if (!isFormValid())
            return;

        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);

        ChangeCityOccupationRequestModel body = new ChangeCityOccupationRequestModel();
        body.setKota_user(city.getText().toString());

        ChangeCityOccupationTask task = new ChangeCityOccupationTask(activity) {
            @Override
            public void onSuccess() {
                    progress.dismissAllowingStateLoss();
                activity.onBackClick();
            }

            @Override
            public void onFailed() {
                    progress.dismissAllowingStateLoss();
                Toast.makeText(activity, "Failed to change user name", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(body);
    }

    @OnClick(R.id.city_layout)
    public void onCityLayoutClick() {
        String jsonCity = PreferenceManager.getString(activity, Constant.DATA_KOTA, "");

        List2Dialog reportDialog = new List2Dialog() {
            @Override
            public void onSelectedItem(String item) {
                city.setText(item);
                dismiss();
            }
        };

        Bundle bundle = new Bundle();
        bundle.putString("title", "City");
        bundle.putString("data", jsonCity);

        reportDialog.setArguments(bundle);
        reportDialog.show(getFragmentManager(), null);
    }

    private boolean isFormValid() {
        String mName = city.getText().toString();
        boolean status = true;

        if (mName.equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please verify your city.", Toast.LENGTH_SHORT).show();
            status = false;
        }
        return status;
    }
}
