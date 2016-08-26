package com.maxxcoffee.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maxxcoffee.mobile.fragment.DetailPointHistoryFragment;
import com.maxxcoffee.mobile.fragment.DetailTopupHistoryFragment;
import com.maxxcoffee.mobile.fragment.DetailTransactionHistoryFragment;
import com.maxxcoffee.mobile.fragment.DetailTransferHistoryFragment;
import com.maxxcoffee.mobile.fragment.PointHistoryFragment;
import com.maxxcoffee.mobile.fragment.TransactionHistoryFragment;
import com.maxxcoffee.mobile.fragment.TransferHistoryFragment;

/**
 * Created by Rio Swarawan on 5/22/2016.
 */
public class DetailHistoryPagerAdapter extends FragmentPagerAdapter {

    private static final String[] HISTORY_MENU = {"Transaction", "Beans", "Transfer", "Top Up"};

    public DetailHistoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new DetailTransactionHistoryFragment();
                break;
            case 1:
                fragment = new DetailPointHistoryFragment();
                break;
            case 2:
                fragment = new DetailTransferHistoryFragment();
                break;
            case 3:
                fragment = new DetailTopupHistoryFragment();
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
