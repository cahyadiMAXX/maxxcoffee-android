package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.request.RegisterCardRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class RegisterCardTask extends AsyncTask<RegisterCardRequestModel, Boolean, CardResponseModel> {

    private Context context;

    public RegisterCardTask(Context context) {
        this.context = context;
    }

    @Override
    protected CardResponseModel doInBackground(RegisterCardRequestModel... data) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        RegisterCardRequestModel body = data[0];
        body.setToken(token);

        ApiManager.getApiInterface(context).registerCard(accessToken, body, new Callback<CardResponseModel>() {
            @Override
            public void success(CardResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(CardResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success")) {
                onSuccess();
            } else {
                if (response.getMessages() != null){
                    //Toast.makeText(context, response.getMessages(), Toast.LENGTH_LONG).show();
                    onFailed(response.getMessages());
                }else{
                    onFailed();
                }
            }
        }
    }

    public abstract void onSuccess();

    public abstract void onFailed();

    public abstract void onFailed(String message);
}
