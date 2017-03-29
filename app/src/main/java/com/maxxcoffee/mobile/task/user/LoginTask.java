package com.maxxcoffee.mobile.task.user;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.LoginRequestModel;
import com.maxxcoffee.mobile.model.response.LoginResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by rioswarawan on 7/8/16.
 */
public abstract class LoginTask extends AsyncTask<LoginRequestModel, Boolean, LoginResponseModel> {

    private Context context;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected LoginResponseModel doInBackground(LoginRequestModel... loginRequestModels) {
        LoginRequestModel body = loginRequestModels[0];

        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        ApiManager.getApiInterface(context).login(accessToken, body, new Callback<LoginResponseModel>() {
            @Override
            public void success(LoginResponseModel loginResponseModel, Response response) {
                onPostExecute(loginResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e("LoginTask %s", error.toString());
                onFailed("Something went wrong. Please try again.");
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(LoginResponseModel loginResponseModel) {
        super.onPostExecute(loginResponseModel);
        try {
            if (loginResponseModel != null) {
                Timber.e("loginResponseModel %s", loginResponseModel.toString());
                String status = loginResponseModel.getStatus();
                if (status.equalsIgnoreCase("success")) {
                    PreferenceManager.putString(context, Constant.PREFERENCE_TOKEN, loginResponseModel.getToken());
                    PreferenceManager.putString(context, Constant.PREFERENCE_USER_NAME, loginResponseModel.getUsername());
                    PreferenceManager.putString(context, Constant.PREFERENCE_BALANCE, loginResponseModel.getBalance_total());
                    PreferenceManager.putString(context, Constant.PREFERENCE_BEAN, loginResponseModel.getBeans());
                    onSuccess(status);
                } else if(status.equalsIgnoreCase("fail")){
                    String message = loginResponseModel.getMessages();
                    onFailed(message);
                }
            }
        }catch (Exception e){
            onFailed(e.toString());
        }
    }

    public abstract void onSuccess(String loginResponseModel);

    public abstract void onFailed(String loginResponseModel);
}
