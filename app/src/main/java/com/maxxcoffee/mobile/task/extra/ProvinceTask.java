package com.maxxcoffee.mobile.task.extra;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.database.controller.ProvinceController;
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
public abstract class ProvinceTask extends AsyncTask<Void, Boolean, KotaResponseModel> {

    private Context context;
    private ProvinceController provinceController;

    public ProvinceTask(Context context) {
        this.context = context;
        this.provinceController = new ProvinceController(context);
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
        if (response != null) {
            for (KotaItemResponseModel kota : response.getListprovinsi()) {
                ProvinceEntity entity = new ProvinceEntity();
                entity.setName(kota.getNama_provinsi());

                provinceController.insert(entity);
            }

            List<String> jsonKota = new ArrayList<>();
            List<ProvinceEntity> cities = provinceController.getCity();
            for (ProvinceEntity kota : cities) {
                jsonKota.add(kota.getName());
            }
            onSuccess(new Gson().toJson(jsonKota));
        }
    }

    public abstract void onSuccess(String json);

    public abstract void onFailed();
}
