package com.maxxcoffee.mobile.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.FormActivity;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.MenuAdapter;
import com.maxxcoffee.mobile.database.controller.MenuCategoryController;
import com.maxxcoffee.mobile.database.controller.MenuController;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;
import com.maxxcoffee.mobile.fragment.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/25/2016.
 */
public class MenuDrinkFragment extends Fragment {

    @Bind(R.id.menu_list)
    ExpandableListView menuList;

    private MainActivity activity;
    private List<MenuCategoryEntity> listDataHeader;
    private HashMap<MenuCategoryEntity, List<List<MenuEntity>>> listDataChild;
    private MenuAdapter adapter;
    private MenuController controller;
    private MenuCategoryController categoryController;
    private int lastExpandedPosition = -1;

    public MenuDrinkFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        controller = new MenuController(activity);
        categoryController = new MenuCategoryController(activity);
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        adapter = new MenuAdapter(activity, listDataHeader, listDataChild) {
            @Override
            protected void onRightMenuClick(MenuEntity menu) {
                Bundle bundle = new Bundle();
                bundle.putInt("menu-id", menu.getId());
                bundle.putString("title", menu.getCategory());

                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.DETAIL_MENU);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected void onLeftMenuClick(MenuEntity menu) {
                Bundle bundle = new Bundle();
                bundle.putInt("menu-id", menu.getId());
                bundle.putString("title", menu.getCategory());

                Intent intent = new Intent(activity, FormActivity.class);
                intent.putExtra("content", FormActivity.DETAIL_MENU);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menulist, container, false);

        ButterKnife.bind(this, view);

        menuList.setAdapter(adapter);
        menuList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    menuList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        fetchingData();

        return view;
    }

    private void fetchingData() {
        listDataHeader.clear();
        listDataChild.clear();

        /*LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        List<MenuCategoryEntity> groups = categoryController.getMenuCategoryDrinks();
        for (MenuCategoryEntity group : groups) {
            listDataHeader.add(group);

            List<MenuEntity> modelMenu = controller.getMenuItemByCategory(group.getId());
            List<MenuEntity> temp = null;
            List<List<MenuEntity>> menuList = new ArrayList<>();

            for (int position = 0; position < modelMenu.size(); position++) {
                MenuEntity menu = modelMenu.get(position);

                if (position % 2 == 0) {
                    temp = new ArrayList<>();
                    temp.add(menu);

                    if (position == modelMenu.size() - 1) {
                        menuList.add(temp);
                    }
                } else {
                    assert temp != null;
                    temp.add(menu);
                    menuList.add(temp);
                }
                listDataChild.put(group, menuList);
            }
            adapter.notifyDataSetChanged();
        }
        //progress.dismissAllowingStateLoss();
        if (loading.isShowing())loading.dismiss();
    }
}
