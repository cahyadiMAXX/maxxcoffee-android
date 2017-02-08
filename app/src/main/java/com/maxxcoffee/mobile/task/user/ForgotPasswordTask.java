package com.maxxcoffee.mobile.task.user;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.ChangePhoneRequestModel;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class ForgotPasswordTask extends AsyncTask<ChangePhoneRequestModel, Boolean, DefaultResponseModel> {

    private Context context;

    public ForgotPasswordTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(ChangePhoneRequestModel... data) {

        ChangePhoneRequestModel body = data[0];

        ApiManager.getApiInterface(context).forgotPassword(body, new Callback<DefaultResponseModel>() {
            @Override
            public void success(DefaultResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(DefaultResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success")) {
                onSuccess();
            } else if(response.getStatus().equalsIgnoreCase("fail")){
                onFailed(response.getMessages());
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();

    public abstract void onFailed(String message);
}
