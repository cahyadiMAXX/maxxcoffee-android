package com.maxxcoffee.mobile.task.profile;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.ProfileResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/8/16.
 */
public abstract class ProfileTask extends AsyncTask<Void, Boolean, ProfileResponseModel> {

    private Context context;

    public ProfileTask(Context context) {
        this.context = context;
    }

    @Override
    protected ProfileResponseModel doInBackground(Void... voids) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);

        ApiManager.getApiInterface(context).profile(accessToken, body, new Callback<ProfileResponseModel>() {
            @Override
            public void success(ProfileResponseModel profileResponseModel, Response response) {
                onPostExecute(profileResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(ProfileResponseModel profileResponseModel) {
        super.onPostExecute(profileResponseModel);
        if(profileResponseModel != null){
            String status = profileResponseModel.getStatus();
            if (status.equalsIgnoreCase("success")) {
                onSuccess(profileResponseModel);
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess(ProfileResponseModel profileResponseModel);

    public abstract void onFailed();
}
