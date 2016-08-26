package com.maxxcoffee.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

/**
 * Created by rioswarawan on 8/4/16.
 */
public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean welcome = PreferenceManager.getBool(this, Constant.PREFERENCE_WELCOME_SKIP, false);
        if (welcome)
            goToApps();
        else
            goToWelcomePage();
    }

    private void goToApps() {
        PreferenceManager.putBool(this, Constant.PREFERENCE_WELCOME_SKIP, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToWelcomePage() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
