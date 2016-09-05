package com.maxxcoffee.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.fragment.CardDetailHistoryFragment;
import com.maxxcoffee.mobile.fragment.ChangeCityFragment;
import com.maxxcoffee.mobile.fragment.ChangeEmailFragment;
import com.maxxcoffee.mobile.fragment.ChangeNameFragment;
import com.maxxcoffee.mobile.fragment.ChangeOccupationFragment;
import com.maxxcoffee.mobile.fragment.ChangeOldPhoneFragment;
import com.maxxcoffee.mobile.fragment.ChangePasswordFragment;
import com.maxxcoffee.mobile.fragment.ChangePhoneFragment;
import com.maxxcoffee.mobile.fragment.DeleteCardFragment;
import com.maxxcoffee.mobile.fragment.DetailEventFragment;
import com.maxxcoffee.mobile.fragment.DetailPromoFragment;
import com.maxxcoffee.mobile.fragment.FaqDetailFragment;
import com.maxxcoffee.mobile.fragment.ForgotPasswordFragment;
import com.maxxcoffee.mobile.fragment.MenuDetailFragment;
import com.maxxcoffee.mobile.fragment.MyCardDetailFragment;
import com.maxxcoffee.mobile.fragment.RenameCardFragment;
import com.maxxcoffee.mobile.fragment.StoreDetailFragment;

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
    public static final int DETAIL_CARD = 1005;
    public static final int RENAME_CARD = 1006;
    public static final int DELETE_CARD = 1007;
    public static final int CHANGE_PHONE = 1008;
    public static final int HISTORY_DETAIL = 1009;
    public static final int EVENT_DETAIL = 1010;
    public static final int PROMO_DETAIL = 1011;
    public static final int STORE_DETAIL = 1012;
    public static final int CHANGE_CITY = 1013;
    public static final int CHANGE_OCCUPATION = 1014;
    public static final int CHANGE_OLD_PHONE = 1015;
    public static final int FORGOT_PASSWORD = 1016;
    public static final int DETAIL_MENU = 1017;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.refreshform)
    ImageView refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);

        refresh.setVisibility(View.GONE);

        Integer content = getIntent().getIntExtra("content", -999);
        Bundle bundle = getIntent().getExtras();

        switchFragment(content, bundle);
    }

    public void switchFragment(int contentId) {
        switchFragment(contentId, null);
    }

    public void switchFragment(int contentId, Bundle bundle) {
        try {
            //hilangkan dia wkkwkkw
            refresh.setOnClickListener(null);
            refresh.setVisibility(View.GONE);

            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.content, getContent(contentId, bundle));
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRefreshButton(boolean grant){
        if(grant){
            refresh.setVisibility(View.VISIBLE);
        }else{
            refresh.setVisibility(View.GONE);
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
            case CHANGE_PHONE:
                fragment = new ChangePhoneFragment();
                break;
            case FAQ_DETAIL:
                fragment = new FaqDetailFragment();
                break;
            case DETAIL_CARD:
                fragment = new MyCardDetailFragment();
                break;
            case RENAME_CARD:
                fragment = new RenameCardFragment();
                break;
            case DELETE_CARD:
                fragment = new DeleteCardFragment();
                break;
            case HISTORY_DETAIL:
                fragment = new CardDetailHistoryFragment();
                break;
            case EVENT_DETAIL:
                fragment = new DetailEventFragment();
                break;
            case PROMO_DETAIL:
                fragment = new DetailPromoFragment();
                break;
            case STORE_DETAIL:
                fragment = new StoreDetailFragment();
                break;
            case CHANGE_CITY:
                fragment = new ChangeCityFragment();
                break;
            case CHANGE_OCCUPATION:
                fragment = new ChangeOccupationFragment();
                break;
            case CHANGE_OLD_PHONE:
                fragment = new ChangeOldPhoneFragment();
                break;
            case FORGOT_PASSWORD:
                fragment = new ForgotPasswordFragment();
                break;
            case DETAIL_MENU:
                fragment = new MenuDetailFragment();
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

    public ImageView getRefresh() {
        return refresh;
    }
}
