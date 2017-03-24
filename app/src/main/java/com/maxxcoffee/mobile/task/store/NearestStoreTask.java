package com.maxxcoffee.mobile.task.store;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.request.StoreNearMeRequestModel;
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
public abstract class NearestStoreTask extends AsyncTask<String, Boolean, StoreResponseModel> {

    private Context context;

    public NearestStoreTask(Context context) {
        this.context = context;
    }

    @Override
    protected StoreResponseModel doInBackground(String... strings) {
        String lat = strings[0];
        String ltg = strings[1];

        StoreNearMeRequestModel body = new StoreNearMeRequestModel();
        body.setLatitude(lat);
        body.setLongitude(ltg);

        ApiManager.getApiInterface(context).storeNearMe(body, new Callback<StoreResponseModel>() {
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
                    onSuccess(response.getData());
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
