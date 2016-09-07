package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.request.PrimaryCardRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class CardDetailTask extends AsyncTask<PrimaryCardRequestModel, Boolean, CardResponseModel> {

    private Context context;

    public CardDetailTask(Context context) {
        this.context = context;
    }

    @Override
    protected CardResponseModel doInBackground(PrimaryCardRequestModel... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        PrimaryCardRequestModel body = data[0];
        body.setToken(token);

        //lihat lagi mas
        ApiManager.getApiInterface(context).cardListDetail(accessToken, body, new Callback<CardResponseModel>() {
            @Override
            public void success(CardResponseModel storeResponseModel, Response response) {
                //Log.d("storeResponseModel", storeResponseModel.toString());
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
    protected void onPostExecute(CardResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success")) {
                onSuccess(response.getResult());
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess(List<CardItemResponseModel> responseModel);

    public abstract void onFailed();
}