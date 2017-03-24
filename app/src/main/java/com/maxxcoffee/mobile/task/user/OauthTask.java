package com.maxxcoffee.mobile.task.user;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.OauthRequestModel;
import com.maxxcoffee.mobile.model.response.OauthResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/8/16.
 */
public abstract class OauthTask extends AsyncTask<OauthRequestModel, Boolean, OauthResponseModel> {

    private Context context;

    public OauthTask(Context context) {
        this.context = context;
    }

    @Override
    protected OauthResponseModel doInBackground(OauthRequestModel... oauthRequestModels) {
        OauthRequestModel body = oauthRequestModels[0];

        ApiManager.getApiInterface(context).oauth(body, new Callback<OauthResponseModel>() {
            @Override
            public void success(OauthResponseModel oauthResponseModel, Response response) {
                onPostExecute(oauthResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(OauthResponseModel oauthResponseModel) {
        super.onPostExecute(oauthResponseModel);
        try {
            if (oauthResponseModel != null) {
                String accessToken = oauthResponseModel.getAccess_token();

                if (accessToken != null) {
                    PreferenceManager.putString(context, Constant.PREFERENCE_ACCESS_TOKEN, "Bearer " + accessToken);
                    onSuccess();
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();
}
