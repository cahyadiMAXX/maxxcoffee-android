package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.ChangePhoneRequestModel;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.ChangeUserDataResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class ChangePhoneTask extends AsyncTask<ChangePhoneRequestModel, Boolean, DefaultResponseModel> {

    private Context context;

    public ChangePhoneTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(ChangePhoneRequestModel... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        ChangePhoneRequestModel body = data[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).changePhone(accessToken, body, new Callback<DefaultResponseModel>() {
            @Override
            public void success(DefaultResponseModel changeUserDataResponseModel, Response response) {
                onPostExecute(changeUserDataResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(DefaultResponseModel changeUserDataResponseModel) {
        super.onPostExecute(changeUserDataResponseModel);
        if (changeUserDataResponseModel != null) {
            if (changeUserDataResponseModel.getStatus().equals("success")) {
                onSuccess();
            } else {
                onWait();
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onWait();

    public abstract void onFailed();
}
