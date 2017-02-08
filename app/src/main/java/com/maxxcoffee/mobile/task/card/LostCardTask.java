package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.request.LostCardRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
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
public abstract class LostCardTask extends AsyncTask<LostCardRequestModel, Boolean, DefaultResponseModel> {

    private Context context;

    public LostCardTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(LostCardRequestModel... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        LostCardRequestModel body = data[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).lostCard(accessToken, body, new Callback<DefaultResponseModel>() {
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
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();
}
