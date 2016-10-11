package com.maxxcoffee.mobile.api;

import android.content.Context;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.BuildConfig;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.Utils;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
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
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
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
