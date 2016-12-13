package com.maxxcoffee.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rioswarawan on 8/4/16.
 */
public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (!Utils.isAllowed()){
            PreferenceManager.clearPreference(LauncherActivity.this);
            DatabaseConfig db = new DatabaseConfig(LauncherActivity.this);
            db.clearAllTable();
            //langsung
            goToApps();
        } else {*/
            boolean welcome = PreferenceManager.getBool(this, Constant.PREFERENCE_WELCOME_SKIP, false);
            if (welcome)
                goToApps();
            else
                goToWelcomePage();
        //}
    }

    private void goToApps() {

        PreferenceManager.putBool(this, Constant.PREFERENCE_WELCOME_SKIP, true);

        checkRating();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToWelcomePage() {
        Intent intent = new Intent(this, WelcomeSliderActivity.class);

        checkRating();

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void checkRating(){
        boolean has_launch = PreferenceManager.getBool(this, Constant.PREFERENCE_HAS_LAUNCH, false);
        if(!has_launch){
            // Get date of first launch
            SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
            Date today = new Date();
            String strToday = df.format(today);
            String date_firstLaunch = PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_DATE_FIRST_LAUNCH, strToday);
            Date inputDate = null;
            try {
                inputDate = df.parse(date_firstLaunch);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (Utils.getDurationInMinutes(inputDate) == 0) {
                date_firstLaunch = strToday;
                PreferenceManager.putString(getApplicationContext(), Constant.PREFERENCE_DATE_FIRST_LAUNCH, date_firstLaunch);
                PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_HAS_LAUNCH, true);
            }
        }
    }
}
