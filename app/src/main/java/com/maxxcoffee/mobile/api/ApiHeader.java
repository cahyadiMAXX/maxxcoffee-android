package com.maxxcoffee.mobile.api;

import retrofit.RequestInterceptor;

/**
 * Created by rioswarawan on 7/6/16.
 */
public class ApiHeader implements RequestInterceptor {

    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        request.addHeader("Accept", "application/json");
        request.addHeader("Connection", "close");
    }
}
