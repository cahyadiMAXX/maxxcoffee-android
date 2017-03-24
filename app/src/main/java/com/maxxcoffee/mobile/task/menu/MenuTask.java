package com.maxxcoffee.mobile.task.menu;

import android.content.Context;
import android.os.AsyncTask;

import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.model.request.DefaultRequestModel;
import com.maxxcoffee.mobile.model.response.CardItemResponseModel;
import com.maxxcoffee.mobile.model.response.CardResponseModel;
import com.maxxcoffee.mobile.model.response.MenuItemResponseModel;
import com.maxxcoffee.mobile.model.response.MenuResponseModel;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class MenuTask extends AsyncTask<Void, Boolean, MenuResponseModel> {

    private Context context;

    public MenuTask(Context context) {
        this.context = context;
    }

    @Override
    protected MenuResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).menu(new Callback<MenuResponseModel>() {
            @Override
            public void success(MenuResponseModel responseModel, Response response) {
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
    protected void onPostExecute(MenuResponseModel response) {
        super.onPostExecute(response);
        try{
            if (response != null) {
                if (response.getStatus().equals("success")) {
                    onSuccess(response.getMenu());
                } else {
                    onFailed();
                }
            }
        }catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(Map<String, List<MenuItemResponseModel>> responseModel);

    public abstract void onFailed();
}
