package com.maxxcoffee.mobile.task.profile;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.ChangeUserDataResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class ChangeNameTask extends AsyncTask<String, Boolean, ChangeUserDataResponseModel> {

    private Context context;

    public ChangeNameTask(Context context) {
        this.context = context;
    }

    @Override
    protected ChangeUserDataResponseModel doInBackground(String... strings) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");
        String firstName = strings[0];
        String lastName = strings[1];

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);
        body.setNama_user(firstName);
        body.setNama_user_last(lastName);

        ApiManager.getApiInterface(context).changeName(accessToken, body, new Callback<ChangeUserDataResponseModel>() {
            @Override
            public void success(ChangeUserDataResponseModel changeUserDataResponseModel, Response response) {
                onPostExecute(changeUserDataResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                onFailed();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(ChangeUserDataResponseModel changeUserDataResponseModel) {
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
