package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.CardImageResponseModel;
import com.maxxcoffee.mobile.model.response.HomeResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class CardImageTask extends AsyncTask<Void, Boolean, CardImageResponseModel> {

    private Context context;

    public CardImageTask(Context context) {
        this.context = context;
    }

    @Override
    protected CardImageResponseModel doInBackground(Void... voids) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);

        ApiManager.getApiInterface(context).cardImage(accessToken, body, new Callback<CardImageResponseModel>() {
            @Override
            public void success(CardImageResponseModel storeResponseModel, Response response) {
                onPostExecute(storeResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("errror image card", error.toString());
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(CardImageResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            onSuccess(response);
        }
    }

    public abstract void onSuccess(CardImageResponseModel response);

    public abstract void onFailed();
}
