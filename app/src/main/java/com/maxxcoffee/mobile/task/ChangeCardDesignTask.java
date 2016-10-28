package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.CardDesignRequestModel;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.CardImageResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class ChangeCardDesignTask extends AsyncTask<CardDesignRequestModel, Boolean, DefaultResponseModel> {

    private Context context;

    public ChangeCardDesignTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(CardDesignRequestModel... params) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        CardDesignRequestModel body = params[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).changeCardDesign(accessToken, body, new Callback<DefaultResponseModel>() {
            @Override
            public void success(DefaultResponseModel storeResponseModel, Response response) {
                onPostExecute(storeResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("errror card design", error.toString());
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(DefaultResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            onSuccess(response);
        }
    }

    public abstract void onSuccess(DefaultResponseModel response);

    public abstract void onFailed();
}
