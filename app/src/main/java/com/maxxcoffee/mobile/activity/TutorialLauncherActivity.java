package com.maxxcoffee.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

/**
 * Created by rioswarawan on 8/4/16.
 */
public class TutorialLauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showTutorial = PreferenceManager.getBool(this, Constant.PREFERENCE_TUTORIAL_SKIP, false);
        if (showTutorial)
            goToMain();
        else
            goToTutorial();
    }

    private void goToMain() {
        PreferenceManager.putBool(this, Constant.PREFERENCE_TUTORIAL_SKIP, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToTutorial() {
        Intent intent = new Intent(this, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
