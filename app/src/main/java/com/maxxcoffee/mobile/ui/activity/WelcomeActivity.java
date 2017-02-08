package com.maxxcoffee.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.daimajia.slider.library.SliderLayout;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.adapter.WelcomeAdapter;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/31/2016.
 */
public class WelcomeActivity extends FragmentActivity {

    private final Integer[] IMAGES = {
            R.drawable.bg_welcome_1,
            R.drawable.bg_welcome_2,
            R.drawable.bg_welcome_3,
            R.drawable.bg_welcome_4
    };

    private final String[] TITLE = {
            "Find your \nperfect coffee",
            "Find your collection",
            "Find our stores",
            "You are ready!"
    };
    private final String[] SUBTITLE = {
            "Let us show you our customer's favorite",
            "Cool card design and full colour tumbler",
            "We are around you. We are near you",
            "Time to explore MAXX COFFEE. \nSee you"
    };
    private final boolean[] NEXT = {
            false,
            false,
            false,
            true
    };

    @Bind(R.id.slider)
    SliderLayout slider;

    private int previousPosition = 0;
    private int SLIDER_COUNT = IMAGES.length - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        for (int i = 0; i < IMAGES.length; i++) {
            WelcomeAdapter adapter = new WelcomeAdapter(this, IMAGES[i], TITLE[i], SUBTITLE[i], NEXT[i]) {
                @Override
                protected void onNext() {
                    goToApps();
                }
            };
            slider.addSlider(adapter);
        }
        slider.stopAutoCycle();
    }

    private void goToApps() {
        PreferenceManager.putBool(WelcomeActivity.this, Constant.PREFERENCE_WELCOME_SKIP, true);
        Intent intent = new Intent(WelcomeActivity.this, TutorialLauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
