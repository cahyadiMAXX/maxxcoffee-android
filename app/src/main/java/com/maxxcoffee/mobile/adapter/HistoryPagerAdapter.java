package com.maxxcoffee.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maxxcoffee.mobile.ui.cardhistory.PointHistoryFragment;
import com.maxxcoffee.mobile.ui.cardhistory.TopUpHistoryFragment;
import com.maxxcoffee.mobile.ui.cardhistory.TransactionHistoryFragment;
import com.maxxcoffee.mobile.ui.cardhistory.TransferHistoryFragment;

/**
 * Created by Rio Swarawan on 5/22/2016.
 */
public class HistoryPagerAdapter extends FragmentPagerAdapter {

    private static final String[] HISTORY_MENU = {"Transaction", "Beans", "Transfer", "Top Up"};

    public HistoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TransactionHistoryFragment();
                break;
            case 1:
                fragment = new PointHistoryFragment();
                break;
            case 2:
                fragment = new TransferHistoryFragment();
                break;
            case 3:
                fragment = new TopUpHistoryFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return HISTORY_MENU.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return HISTORY_MENU[position];
    }
}
