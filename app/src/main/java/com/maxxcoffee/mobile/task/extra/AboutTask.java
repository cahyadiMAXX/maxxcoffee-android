package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.AboutResponseModel;
import com.maxxcoffee.mobile.model.response.AboutResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class AboutTask extends AsyncTask<Void, Boolean, AboutResponseModel> {

    private Context context;

    public AboutTask(Context context) {
        this.context = context;
    }

    @Override
    protected AboutResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).about(new Callback<AboutResponseModel>() {
            @Override
            public void success(AboutResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(AboutResponseModel response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getAbout());
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(String about);

    public abstract void onFailed();
}
