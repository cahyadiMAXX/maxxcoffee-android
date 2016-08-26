package com.maxxcoffee.mobile.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.fragment.dialog.OptionDialog;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends FragmentActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.navbar)
    LinearLayout navbar;
    @Bind(R.id.toolbar_layout)
    RelativeLayout toolbarLayout;
    @Bind(R.id.root_layout)
    LinearLayout rootLayout;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.hamburger)
    ImageView hamburger;
    @Bind(R.id.tutorial_1)
    RelativeLayout tutorial1;
    @Bind(R.id.tutorial_2)
    RelativeLayout tutorial2;
    @Bind(R.id.tutorial_3)
    FrameLayout tutorial3;
    @Bind(R.id.tutorial_4)
    LinearLayout tutorial4;
    @Bind(R.id.signup_layout)
    LinearLayout signup;

    private int mDayPart = Utils.getDayPart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_tutorial);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground(mDayPart);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            endTutorial();
        } else {
            endTutorial();
        }
    }

    private void endTutorial() {
        Bundle bundle = new Bundle();
        bundle.putString("content", "End this tutorial?");
        bundle.putString("default", OptionDialog.CANCEL);

        OptionDialog optionDialog = new OptionDialog() {
            @Override
            protected void onOk() {
                finish();
            }

            @Override
            protected void onCancel() {
                dismiss();
            }
        };
        optionDialog.setArguments(bundle);
        optionDialog.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.tutorial_1)
    public void onTutorial1Click() {
        tutorial1.setVisibility(View.GONE);
        tutorial2.setVisibility(View.VISIBLE);
        tutorial3.setVisibility(View.GONE);
        tutorial4.setVisibility(View.GONE);
    }

    @OnClick(R.id.tutorial_2)
    public void onTutorial2Click() {
        drawer.openDrawer(GravityCompat.START);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        tutorial1.setVisibility(View.GONE);
        tutorial2.setVisibility(View.GONE);
        tutorial3.setVisibility(View.VISIBLE);
        tutorial4.setVisibility(View.GONE);
    }

    @OnClick(R.id.tutorial_3)
    public void onTutorial3Click() {
        tutorial1.setVisibility(View.GONE);
        tutorial2.setVisibility(View.GONE);
        tutorial3.setVisibility(View.GONE);
        tutorial4.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            signup.setBackground(getResources().getDrawable(android.R.color.transparent, null));
        } else {
            signup.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @OnClick(R.id.layout_finish)
    public void onTutorial4Click() {
        PreferenceManager.putBool(this, Constant.PREFERENCE_TUTORIAL_SKIP, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setBackground(int dayPart) {
        int background = R.drawable.bg_morning;
        int nav = R.drawable.bg_morning_navbar;

        if (dayPart == Utils.MORNING) {
            background = R.drawable.bg_morning;
            nav = R.drawable.bg_morning_navbar;
        } else if (dayPart == Utils.AFTERNOON) {
            background = R.drawable.bg_afternoon;
            nav = R.drawable.bg_afternoon_navbar;
        } else if (dayPart == Utils.EVENING) {
            background = R.drawable.bg_evening;
            nav = R.drawable.bg_evening_navbar;
        }

        if (rootLayout != null) {
            rootLayout.setBackgroundResource(background);
        }
        if (navbar != null) {
            navbar.setBackgroundResource(nav);
        }
    }

}
