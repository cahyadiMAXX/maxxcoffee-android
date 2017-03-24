package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
import com.maxxcoffee.mobile.model.response.PromoItemResponseModel;
import com.maxxcoffee.mobile.model.response.PromoResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class PromoTask extends AsyncTask<Void, Boolean, PromoResponseModel> {

    private Context context;

    public PromoTask(Context context) {
        this.context = context;
    }

    @Override
    protected PromoResponseModel doInBackground(Void... voids) {
        ApiManager.getApiInterface(context).promo(new Callback<PromoResponseModel>() {
            @Override
            public void success(PromoResponseModel responseModel, Response response) {
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
    protected void onPostExecute(PromoResponseModel response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getResult());
                } else if (response.getStatus().equals("fail")) {
                    onEmpty();
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(List<PromoItemResponseModel> responseModel);

    public abstract void onEmpty();

    public abstract void onFailed();
}
