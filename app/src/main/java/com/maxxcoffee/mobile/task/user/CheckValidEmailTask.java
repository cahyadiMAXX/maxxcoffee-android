package com.maxxcoffee.mobile.task.user;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.CheckValidEmailRequestModel;
import com.maxxcoffee.mobile.model.response.CheckValidEmailResponseModel;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vourest on 8/31/16.
 */
public abstract class CheckValidEmailTask extends AsyncTask<CheckValidEmailRequestModel, Boolean, CheckValidEmailResponseModel> {
    private Context context;

    public CheckValidEmailTask(Context context) {
        this.context = context;
    }

    @Override
    protected CheckValidEmailResponseModel doInBackground(CheckValidEmailRequestModel... validEmail) {
        CheckValidEmailRequestModel body = validEmail[0];

        ApiManager.getApiInterface(context).checkEmailExist(body, new Callback<CheckValidEmailResponseModel>() {
            @Override
            public void success(CheckValidEmailResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(CheckValidEmailResponseModel response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                if (response.getStatus().contains("not registered")) {
                    onSuccess(response.getStatus());
                } else if (response.getStatus().contains("exists")){
                    onFailed(response.getStatus());
                }else {
                    onFailed();
                }
            }
        }catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(String response);

    public abstract void onFailed();

    public abstract void onFailed(String response);
}
