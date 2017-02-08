package com.maxxcoffee.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maxxcoffee.mobile.ui.store.NearMeFragment;
import com.maxxcoffee.mobile.ui.store.OurStoreFragment;

/**
 * Created by Rio Swarawan on 5/22/2016.
 */
public class StorePagerAdapter extends FragmentPagerAdapter {

    private static final String[] STORE_MENU = {"Near Me", "Our Stores"};

    public StorePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new NearMeFragment();
                break;
            case 1:
                fragment = new OurStoreFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return STORE_MENU.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return STORE_MENU[position];
    }
}
