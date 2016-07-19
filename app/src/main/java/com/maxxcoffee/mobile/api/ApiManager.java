package com.maxxcoffee.mobile.api;

import android.content.Context;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.Utils;

import java.util.Properties;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ApiManager {


    private static ApiHeader apiHeader;
    private static ApiInterface apiInterface;
    private static RestAdapter restAdapter;
    private static Properties properties;

    public static ApiInterface getApiInterface(Context context) {
        properties = Utils.getProperties(context);
        restAdapter = getRestAdapter();
        if (apiInterface == null) {
            apiInterface = restAdapter.create(ApiInterface.class);
        }
        return apiInterface;
    }

    private static RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
                    .setRequestInterceptor(getRequestInterceptor())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(properties.getProperty("url-stagging"))
                    .setConverter(new GsonConverter(new Gson()))
                    .build();
        }
        return restAdapter;
    }

    private static RequestInterceptor getRequestInterceptor() {
        if (apiHeader == null) {
            apiHeader = new ApiHeader();
        }
        return apiHeader;
    }
}