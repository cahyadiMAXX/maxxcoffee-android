package com.maxxcoffee.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maxxcoffee.mobile.ui.menu.MenuDrinkFragment;
import com.maxxcoffee.mobile.ui.menu.MenuFoodFragment;
import com.maxxcoffee.mobile.ui.menu.MenuMerchandiseFragment;

/**
 * Created by Rio Swarawan on 5/22/2016.
 */
public class MenuPagerAdapter extends FragmentPagerAdapter {

    private static final String[] STORE_MENU = {"Drink", "Food", "Merchandise"};

    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MenuDrinkFragment();
                break;
            case 1:
                fragment = new MenuFoodFragment();
                break;
            case 2:
                fragment = new MenuMerchandiseFragment();
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
