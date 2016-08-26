package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.model.response.HomeResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class HomeTask extends AsyncTask<Void, Boolean, HomeResponseModel> {

    private Context context;

    public HomeTask(Context context) {
        this.context = context;
    }

    @Override
    protected HomeResponseModel doInBackground(Void... voids) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);

        ApiManager.getApiInterface(context).home(accessToken, body, new Callback<HomeResponseModel>() {
            @Override
            public void success(HomeResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(HomeResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            onSuccess(response);
        }
    }

    public abstract void onSuccess(HomeResponseModel response);

    public abstract void onFailed();
}
