package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.response.EventItemResponseModel;
import com.maxxcoffee.mobile.model.response.EventResponseModel;
import com.maxxcoffee.mobile.model.response.PromoItemResponseModel;
import com.maxxcoffee.mobile.model.response.PromoResponseModel;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class EventTask extends AsyncTask<Void, Boolean, EventResponseModel> {

    private Context context;

    public EventTask(Context context) {
        this.context = context;
    }

    @Override
    protected EventResponseModel doInBackground(Void... voids) {
        ApiManager.getApiInterface(context).event(new Callback<EventResponseModel>() {
            @Override
            public void success(EventResponseModel responseModel, Response response) {
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
    protected void onPostExecute(EventResponseModel response) {
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

    public abstract void onSuccess(List<EventItemResponseModel> responseModel);

    public abstract void onEmpty();

    public abstract void onFailed();
}
