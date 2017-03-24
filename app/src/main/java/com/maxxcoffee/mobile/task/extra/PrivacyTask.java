package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.response.PrivacyResponseModel;
import com.maxxcoffee.mobile.model.response.TosResponseModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class PrivacyTask extends AsyncTask<Void, Boolean, PrivacyResponseModel> {

    private Context context;

    public PrivacyTask(Context context) {
        this.context = context;
    }

    @Override
    protected PrivacyResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).privacyPolicy(new Callback<PrivacyResponseModel>() {
            @Override
            public void success(PrivacyResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(PrivacyResponseModel response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getResult());
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(String model);

    public abstract void onFailed();
}
