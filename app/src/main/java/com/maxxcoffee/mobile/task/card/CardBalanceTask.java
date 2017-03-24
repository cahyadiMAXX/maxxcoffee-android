package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
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
public abstract class CardBalanceTask extends AsyncTask<Void, Boolean, CardResponseModel> {

    private Context context;

    public CardBalanceTask(Context context) {
        this.context = context;
    }

    @Override
    protected CardResponseModel doInBackground(Void... voids) {
        String token = PreferenceManager.getString(context, Constant.PREFERENCE_TOKEN, "");
        String accessToken = PreferenceManager.getString(context, Constant.PREFERENCE_ACCESS_TOKEN, "");

        DefaultRequestModel body = new DefaultRequestModel();
        body.setToken(token);

        //lihat lagi mas
        ApiManager.getApiInterface(context).cardList(accessToken, body, new Callback<CardResponseModel>() {
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
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getResult());
                } else {
                    onFailed();
                }
            }
        }catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(List<CardItemResponseModel> responseModel);

    public abstract void onFailed();
}
