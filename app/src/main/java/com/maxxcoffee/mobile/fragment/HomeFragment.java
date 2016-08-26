package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;
import com.maxxcoffee.mobile.model.response.HomeResponseModel;
import com.maxxcoffee.mobile.task.HomeTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Bind(R.id.greeting)
    TextView greeting;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.balance)
    TextView balance;
    @Bind(R.id.point)
    TextView point;

    private MainActivity activity;
    private String token;
    private int mDayPart = Utils.getDayPart();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("");

        getLocalData();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                fetchingData();
            }
        });
        swipe.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    fetchingData();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);
    }

    private void getLocalData() {
        String mName = PreferenceManager.getString(activity, Constant.PREFERENCE_USER_NAME, "");
        String mBalance = PreferenceManager.getString(activity, Constant.PREFERENCE_BALANCE, "");
        String mBeans = PreferenceManager.getString(activity, Constant.PREFERENCE_BEAN, "");

        setGreeting(mDayPart);
        name.setText(mName);
        balance.setText(mBalance.equals("") ? "" : "IDR " + mBalance);
        point.setText(mBeans.equals("") ? "" : mBeans + " Beans");
    }

    private void fetchingData() {
        final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);
//        final TBaseProgress progress = new TBaseProgress(activity);
//        progress.show();
//        swipe.setRefreshing(true);

        HomeTask task = new HomeTask(activity) {
            @Override
            public void onSuccess(HomeResponseModel response) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_USER_NAME, response.getUsername());
                PreferenceManager.putString(activity, Constant.PREFERENCE_BALANCE, String.valueOf(response.getBalance()));
                PreferenceManager.putString(activity, Constant.PREFERENCE_BEAN, String.valueOf(response.getPoint()));
                getLocalData();

//                swipe.setRefreshing(false);
                progress.dismissAllowingStateLoss();
            }

            @Override
            public void onFailed() {
//                swipe.setRefreshing(false);
                progress.dismissAllowingStateLoss();
            }
        };
        task.execute();
    }

    private void setGreeting(int daypart) {
        boolean isLoggedIn = PreferenceManager.getBool(activity, Constant.PREFERENCE_LOGGED_IN, false);
        String hello = "";
        if (daypart == Utils.MORNING) {
            hello = "Good Morning";
        } else if (daypart == Utils.AFTERNOON) {
            hello = "Good Afternoon";
        } else if (daypart == Utils.EVENING) {
            hello = "Good Evening";
        }
        greeting.setText(hello + (isLoggedIn ? "," : ""));
    }

    private void setBackground(int dayPart) {
        int background = R.drawable.bg_morning;
        int navbar = R.drawable.bg_morning_navbar;

        if (dayPart == Utils.MORNING) {
            background = R.drawable.bg_morning;
            navbar = R.drawable.bg_morning_navbar;
        } else if (dayPart == Utils.AFTERNOON) {
            background = R.drawable.bg_afternoon;
            navbar = R.drawable.bg_afternoon_navbar;
        } else if (dayPart == Utils.EVENING) {
            background = R.drawable.bg_evening;
            navbar = R.drawable.bg_evening_navbar;
        }
        activity.setRootBackground(background);
        activity.setNavbarBackground(navbar);
    }

}
