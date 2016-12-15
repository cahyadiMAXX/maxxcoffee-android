package com.maxxcoffee.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.response.FeaturedResponseModel;
import com.maxxcoffee.mobile.task.FeaturedControlTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import timber.log.Timber;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                Intent in = new Intent(getApplicationContext(), LauncherActivity.class);
                startActivity(in);
            }
        }, 500);
    }

}
