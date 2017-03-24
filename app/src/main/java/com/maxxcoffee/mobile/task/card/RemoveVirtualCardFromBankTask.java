package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.MarkVirtualCardRequestModel;
import com.maxxcoffee.mobile.model.response.DefaultResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class RemoveVirtualCardFromBankTask extends AsyncTask<String, Boolean, DefaultResponseModel> {

    private Context context;

    public RemoveVirtualCardFromBankTask(Context context) {
        this.context = context;
    }

    @Override
    protected DefaultResponseModel doInBackground(String... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        MarkVirtualCardRequestModel body = new MarkVirtualCardRequestModel();
        body.setDistribution_id(data[0]);
        body.setToken(token);

        ApiManager.getApiInterface(context).markAsVirtualCard(accessToken, body, new Callback<DefaultResponseModel>() {
            @Override
            public void success(DefaultResponseModel responseModel, Response response) {
                onPostExecute(responseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(DefaultResponseModel changeUserDataResponseModel) {
        super.onPostExecute(changeUserDataResponseModel);
        try {
            if (changeUserDataResponseModel != null) {
                if (changeUserDataResponseModel.getStatus().equals("success")) {
                    onSuccess();
                } else {
                    onFailed();
                }
            }
        }catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();
}
