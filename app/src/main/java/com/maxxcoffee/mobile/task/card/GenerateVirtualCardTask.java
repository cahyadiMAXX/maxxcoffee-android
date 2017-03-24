package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.AddVirtualResponseModel;
import com.maxxcoffee.mobile.model.response.TosResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class GenerateVirtualCardTask extends AsyncTask<Void, Boolean, AddVirtualResponseModel> {

    private Context context;

    public GenerateVirtualCardTask(Context context) {
        this.context = context;
    }

    @Override
    protected AddVirtualResponseModel doInBackground(Void... voids) {

        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);

        ApiManager.getApiInterface(context).generateVirtualCard(accessToken, body, new Callback<AddVirtualResponseModel>() {
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
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response);
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(AddVirtualResponseModel tos);

    public abstract void onFailed();
}
