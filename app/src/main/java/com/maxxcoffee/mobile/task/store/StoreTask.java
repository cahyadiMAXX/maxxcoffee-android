package com.maxxcoffee.mobile.task.store;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.ChangeUserDataResponseModel;
import com.maxxcoffee.mobile.model.response.StoreItemResponseModel;
import com.maxxcoffee.mobile.model.response.StoreResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class StoreTask extends AsyncTask<Void, Boolean, StoreResponseModel> {

    private Context context;

    public StoreTask(Context context) {
        this.context = context;
    }

    @Override
    protected StoreResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).store(new Callback<StoreResponseModel>() {
            @Override
            public void success(StoreResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(StoreResponseModel response) {
        super.onPostExecute(response);
        try{
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getResult());
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(List<StoreItemResponseModel> responseModel);

    public abstract void onFailed();
}
