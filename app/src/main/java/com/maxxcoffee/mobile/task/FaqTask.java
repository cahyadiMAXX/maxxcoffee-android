package com.maxxcoffee.mobile.task;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.response.FaqItemResponseModel;
import com.maxxcoffee.mobile.model.response.FaqResponseModel;
import com.maxxcoffee.mobile.model.response.FaqResponseModel;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class FaqTask extends AsyncTask<Void, Boolean, FaqResponseModel> {

    private Context context;

    public FaqTask(Context context) {
        this.context = context;
    }

    @Override
    protected FaqResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).faq(new Callback<FaqResponseModel>() {
            @Override
            public void success(FaqResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(FaqResponseModel response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.getStatus().equals("success")) {
                onSuccess(response.getResult());
            } else {
                onFailed();
            }
        }
    }

    public abstract void onSuccess(List<FaqItemResponseModel> response);

    public abstract void onFailed();
}
