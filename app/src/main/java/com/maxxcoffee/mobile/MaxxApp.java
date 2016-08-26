package com.maxxcoffee.mobile;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.maxxcoffee.mobile.util.Dummy;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MaxxApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
