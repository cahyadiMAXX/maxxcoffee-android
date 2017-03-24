package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.TransferBalanceRequestModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class TransferBalanceTask extends AsyncTask<TransferBalanceRequestModel, Boolean, DefaultResponseModel> {

    private Context context;

    public TransferBalanceTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(TransferBalanceRequestModel... voids) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        TransferBalanceRequestModel body = voids[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).transferBalance(accessToken, body, new Callback<DefaultResponseModel>() {
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
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess();
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();
}
