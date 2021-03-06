package com.maxxcoffee.mobile.task.card;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.R;
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
public abstract class CardCGITask extends AsyncTask<Void, Boolean, CardResponseModel> {

    private Context context;

    public CardCGITask(Context context) {
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
                //Log.d("storeResponseModel", storeResponseModel.toString());
                onPostExecute(storeResponseModel);
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    switch (error.getResponse().getStatus()){
                        case 401:
                            onFailed("401 request", error.getResponse().getStatus());
                            break;
                        default:
                            onFailed();
                    }
                } catch (Exception e){
                    onFailed(context.getResources().getString(R.string.something_wrong));
                }
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
                } else if(response.getStatus().equalsIgnoreCase("fail")){
                    onFailed(response.getStatus());
                } else{
                    onFailed();
                }
            }
        }catch (Exception e){
            onFailed(e.toString());
        }
    }

    public abstract void onSuccess(List<CardItemResponseModel> responseModel);

    public abstract void onFailed();

    public abstract void onFailed(String message);

    public abstract void onFailed(String message, int request);
}
