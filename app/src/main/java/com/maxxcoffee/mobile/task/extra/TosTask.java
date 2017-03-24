package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.TosResponseModel;
import com.maxxcoffee.mobile.model.response.TosResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class TosTask extends AsyncTask<Void, Boolean, TosResponseModel> {

    private Context context;

    public TosTask(Context context) {
        this.context = context;
    }

    @Override
    protected TosResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).termOfService(new Callback<TosResponseModel>() {
            @Override
            public void success(TosResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(TosResponseModel response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getTos());
                } else {
                    onFailed();
                }
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(String tos);

    public abstract void onFailed();
}
