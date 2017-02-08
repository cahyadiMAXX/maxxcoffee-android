package com.maxxcoffee.mobile.task.user;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.RegisterRequestModel;
import com.maxxcoffee.mobile.model.response.RegisterResponseModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/7/16.
 */
public abstract class RegisterTask extends AsyncTask<RegisterRequestModel, Boolean, RegisterResponseModel> {

    private Context context;

    public RegisterTask(Context context) {
        this.context = context;
    }

    @Override
    protected RegisterResponseModel doInBackground(RegisterRequestModel... registerRequestModels) {
        RegisterRequestModel body = registerRequestModels[0];

        ApiManager.getApiInterface(context).register(body, new Callback<RegisterResponseModel>() {
            @Override
            public void success(RegisterResponseModel registerResponseModel, Response response) {
                onPostExecute(registerResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed("");
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(RegisterResponseModel registerResponseModel) {
        super.onPostExecute(registerResponseModel);
        if (registerResponseModel != null) {
            String status = registerResponseModel.getStatus();
            if (status.equalsIgnoreCase("success")) {
                onSuccess();
            } else if (status.equalsIgnoreCase("fail")) {
                onFailed(registerResponseModel.getMessages());
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed(String message);
}
