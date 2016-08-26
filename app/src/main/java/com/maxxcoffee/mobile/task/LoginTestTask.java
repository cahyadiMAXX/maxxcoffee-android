package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.response.LoginTestResponseModel;
import com.maxxcoffee.mobile.model.response.LoginTestResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/8/16.
 */
public abstract class LoginTestTask extends AsyncTask<LoginRequestModel, Boolean, LoginTestResponseModel> {

    private Context context;

    public LoginTestTask(Context context) {
        this.context = context;
    }

    @Override
    protected LoginTestResponseModel doInBackground(LoginRequestModel... loginRequestModels) {
        LoginRequestModel body = loginRequestModels[0];

        ApiManager.getApiInterface(context).loginTest(body, new Callback<LoginTestResponseModel>() {
            @Override
            public void success(LoginTestResponseModel loginResponseModel, Response response) {
                onPostExecute(loginResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(LoginTestResponseModel loginResponseModel) {
        super.onPostExecute(loginResponseModel);
        if (loginResponseModel != null) {
            String status = loginResponseModel.getStatus();
            String ganti_nomer = loginResponseModel.getGanti_nomer();
            String email = loginResponseModel.getEmail();

            if (status.equalsIgnoreCase("success")) {
                onSuccess();
            } else if (status.equalsIgnoreCase("fail")) {
                onFailed();
            } else if (ganti_nomer.equalsIgnoreCase("yes")) {
                onChangePhoneNumber(email);
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onChangePhoneNumber(String email);

    public abstract void onFailed();
}
