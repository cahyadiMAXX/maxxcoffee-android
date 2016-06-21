package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class HomeFragment extends Fragment {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        activity.setRootBackground(R.drawable.bg_landing);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("");

        int mGreeting = Utils.getDayPart();

        greeting.setText(getDayPartString(mGreeting));
        name.setText("Guntur Satrya Saputro");
        balance.setText("IDR 70.000");
        point.setText("250 Points");

        return view;
    }

    private String getDayPartString(int dayPart) {
        if (dayPart == Utils.MORNING) {
            return "Good Morning,";
        } else if (dayPart == Utils.AFTERNOON) {
            return "Good Afternoon,";
        } else if (dayPart == Utils.EVENING) {
            return "Good Evening,";
        } else if (dayPart == Utils.NIGHT) {
            return "Good Night,";
        }
        return "";
    }

}
