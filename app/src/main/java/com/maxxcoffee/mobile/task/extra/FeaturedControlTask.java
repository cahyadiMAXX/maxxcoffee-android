package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.response.AboutResponseModel;
import com.maxxcoffee.mobile.model.response.FeaturedResponseModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class FeaturedControlTask extends AsyncTask<Void, Boolean, FeaturedResponseModel> {

    private Context context;

    public FeaturedControlTask(Context context) {
        this.context = context;
    }

    @Override
    protected FeaturedResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).featured(new Callback<FeaturedResponseModel>() {
            @Override
            public void success(FeaturedResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(FeaturedResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            onSuccess(response);
        } else {
            onFailed();
        }
    }

    public abstract void onSuccess(FeaturedResponseModel response);

    public abstract void onFailed();
}
