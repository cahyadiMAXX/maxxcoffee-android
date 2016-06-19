package com.maxxcoffee.mobile;

import android.app.Application;

import com.maxxcoffee.mobile.util.Dummy;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MaxxApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Dummy(this);
    }
}
