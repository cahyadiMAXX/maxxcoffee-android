package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.database.controller.CityController;
import com.maxxcoffee.mobile.database.controller.ProvinceController;
import com.maxxcoffee.mobile.database.entity.CityEntity;
import com.maxxcoffee.mobile.database.entity.ProvinceEntity;
import com.maxxcoffee.mobile.model.response.KotaItemResponseModel;
import com.maxxcoffee.mobile.model.response.KotaResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public abstract class CityTask extends AsyncTask<Void, Boolean, KotaResponseModel> {

    private Context context;
    private CityController cityController;

    public CityTask(Context context) {
        this.context = context;
        this.cityController = new CityController(context);
    }

    @Override
    protected KotaResponseModel doInBackground(Void... voids) {

        ApiManager.getApiInterface(context).kota(new Callback<KotaResponseModel>() {
            @Override
            public void success(KotaResponseModel storeResponseModel, Response response) {
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
    protected void onPostExecute(KotaResponseModel response) {
        super.onPostExecute(response);
        try {
            if (response != null) {
                for (KotaItemResponseModel kota : response.getResult()) {
                    CityEntity entity = new CityEntity();
                    entity.setName(kota.getNama_kota());

                    cityController.insert(entity);
                }

                List<String> jsonKota = new ArrayList<>();
                List<CityEntity> cities = cityController.getAllCity();
                for (CityEntity kota : cities) {
                    jsonKota.add(kota.getName());
                }
                onSuccess(new Gson().toJson(jsonKota));
            }
        } catch (Exception e){
            onFailed();
        }
    }

    public abstract void onSuccess(String json);

    public abstract void onFailed();
}
