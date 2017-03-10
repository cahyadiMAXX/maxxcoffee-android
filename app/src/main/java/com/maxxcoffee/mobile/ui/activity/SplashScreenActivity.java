package com.maxxcoffee.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

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

        /*int content = getIntent().getIntExtra("content", 0);
        String contact = getIntent().getStringExtra("contact");

        PreferenceManager.putInt(getApplicationContext(), Constant.PREFERENCE_MAXX_CONTENT, content);
        PreferenceManager.putString(getApplicationContext(), Constant.PREFERENCE_MAXX_CONCAT, contact);*/

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
