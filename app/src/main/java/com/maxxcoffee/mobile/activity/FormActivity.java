package com.maxxcoffee.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.fragment.ChangeEmailFragment;
import com.maxxcoffee.mobile.fragment.ChangeNameFragment;
import com.maxxcoffee.mobile.fragment.ChangePasswordFragment;
import com.maxxcoffee.mobile.fragment.FaqDetailFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 6/5/2016.
 */
public class FormActivity extends FragmentActivity {

    public static final int CHANGE_NAME = 1001;
    public static final int CHANGE_EMAIL = 1002;
    public static final int CHANGE_PASSWORD = 1003;
    public static final int FAQ_DETAIL = 1004;

    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);

        Integer content = getIntent().getIntExtra("content", -999);
        Bundle bundle = getIntent().getExtras();

        switchFragment(content, bundle);

    }

    public void switchFragment(int contentId) {
        switchFragment(contentId, null);
    }

    public void switchFragment(int contentId, Bundle bundle) {
        try {
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.content, getContent(contentId, bundle));
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Fragment getContent(int contentId, Bundle bundle) {
        Fragment fragment = null;
        switch (contentId) {
            case CHANGE_EMAIL:
                fragment = new ChangeEmailFragment();
                break;
            case CHANGE_NAME:
                fragment = new ChangeNameFragment();
                break;
            case CHANGE_PASSWORD:
                fragment = new ChangePasswordFragment();
                break;
            case FAQ_DETAIL:
                fragment = new FaqDetailFragment();
                break;
        }

        if (bundle != null)
            if (fragment != null)
                fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setTitle(String mTitle) {
        title.setText(mTitle);
    }

    @OnClick(R.id.back)
    public void onBackClick() {
        onBackPressed();
    }
}
