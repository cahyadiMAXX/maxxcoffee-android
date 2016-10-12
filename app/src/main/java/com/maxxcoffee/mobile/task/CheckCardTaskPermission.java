package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.AddVirtualResponseModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class CheckCardTaskPermission extends AsyncTask<Void, Boolean, AddVirtualResponseModel> {

    private Context context;

    public CheckCardTaskPermission(Context context) {
        this.context = context;
    }

    @Override
    protected AddVirtualResponseModel doInBackground(Void... voids) {

        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);

        ApiManager.getApiInterface(context).virtualCardPermission(accessToken, body, new Callback<AddVirtualResponseModel>() {
            @Override
            public void success(AddVirtualResponseModel addVirtualResponseModel, Response response) {
                onPostExecute(addVirtualResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(AddVirtualResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success") && response.getPermission().equalsIgnoreCase("not allowed")) {
                onSuccess(response);
            } else if(response.getStatus().equals("success") && response.getPermission().equalsIgnoreCase("allowed")){
                onSuccess();
            } else if(response.getStatus().equals("fail")){
                onFailed(response);
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onSuccess(AddVirtualResponseModel tos);

    public abstract void onFailed();

    public abstract void onFailed(AddVirtualResponseModel tos);
}
