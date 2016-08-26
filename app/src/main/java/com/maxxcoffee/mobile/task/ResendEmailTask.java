package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.ResendEmailRequestModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.ResendEmailSmsResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class ResendEmailTask extends AsyncTask<ResendEmailRequestModel, Boolean, ResendEmailSmsResponseModel> {

    private Context context;

    public ResendEmailTask(Context context) {
        this.context = context;
    }

    @Override
    protected ResendEmailSmsResponseModel doInBackground(ResendEmailRequestModel... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        ResendEmailRequestModel body = data[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).resendEmailSms(accessToken, body, new Callback<ResendEmailSmsResponseModel>() {
            @Override
            public void success(ResendEmailSmsResponseModel storeResponseModel, Response response) {
                onPostExecute(storeResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(ResendEmailSmsResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success")) {
                onSuccess();
            } else {
                onWait(response.getWait_second());
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onWait(String second);

    public abstract void onFailed();

}
