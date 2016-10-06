package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.GCMRequestModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class LogoutAllMyDevicesTask extends AsyncTask<GCMRequestModel, Boolean, DefaultResponseModel> {

    private Context context;

    public LogoutAllMyDevicesTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(GCMRequestModel... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        GCMRequestModel body = data[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).forceLogoutAll(accessToken, body, new Callback<DefaultResponseModel>() {
            @Override
            public void success(DefaultResponseModel storeResponseModel, Response response) {
                Toast.makeText(context, "Your session is expired. Please log in again.", Toast.LENGTH_LONG).show();
                onPostExecute(storeResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("logoutallmydevices", error.toString());
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
                onSuccess(response.getMessages());
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess(String message);

    public abstract void onFailed();
}
