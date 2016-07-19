package com.maxxcoffee.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.activity.MenuDetailActivity;
import com.maxxcoffee.mobile.adapter.MenuAdapter;
import com.maxxcoffee.mobile.database.controller.MenuCategoryController;
import com.maxxcoffee.mobile.database.controller.MenuController;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/25/2016.
 */
public class MenuMerchandiseFragment extends Fragment {

    @Bind(R.id.menu_list)
    ExpandableListView menuList;

    private MainActivity activity;
    private List<MenuCategoryEntity> listDataHeader;
    private HashMap<MenuCategoryEntity, List<List<MenuEntity>>> listDataChild;
    private MenuAdapter adapter;
    private MenuController controller;
    private MenuCategoryController categoryController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        controller = new MenuController(activity);
        categoryController = new MenuCategoryController(activity);


        adapter = new MenuAdapter(activity, listDataHeader, listDataChild) {
            @Override
            protected void onRightMenuClick(MenuEntity menu) {
                Bundle bundle = new Bundle();
                bundle.putInt("menu-id", menu.getId());

                Intent intent = new Intent(activity, MenuDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            protected void onLeftMenuClick(MenuEntity menu) {
                Bundle bundle = new Bundle();
                bundle.putInt("menu-id", menu.getId());

                Intent intent = new Intent(activity, MenuDetailActivity.class);
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

        fetchingData();

        return view;
    }

    private void fetchingData() {
        listDataHeader.clear();
        listDataChild.clear();

        TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        List<MenuCategoryEntity> groups = categoryController.getMenuCategoryMerchandise();
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

        if (progress.isShowing()) {
            progress.dismiss();
        }

//        List<MenuCategoryEntity> categories = menuCategoryController.getMenuCategories();
//        for (MenuCategoryEntity category : categories) {
//            MenuCategoryModel model = new MenuCategoryModel();
//            model.setId(category.getId());
//            model.setName(category.getName());
//
//            listDataHeader.add(model);
//            List<MenuEntity> items = menuItemController.getMenuItemByCategory(model.getId());
//            List<MenuItemModel> temp = null;
//            List<List<MenuItemModel>> itemList = new ArrayList<>();
//            for (int position = 0; position < items.size(); position++) {
//                MenuEntity item = items.get(position);
//                MenuItemModel itemModel = new MenuItemModel();
//                itemModel.setId(item.getId());
//                itemModel.setCategoryId(item.getCategoryId());
//                itemModel.setName(item.getName());
//                itemModel.setDescription(item.getDescription());
//                itemModel.setImage(item.getImage());
//                itemModel.setPrice(item.getPrice());
//                itemModel.setPoint(item.getPoint());
//
////                itemList.add(itemModel);
//                if (position % 2 == 0) {
//                    temp = new ArrayList<>();
//                    temp.add(itemModel);
//
//                    if (position == items.size() - 1) {
//                        itemList.add(temp);
//                    }
//                } else {
//                    assert temp != null;
//                    temp.add(itemModel);
//                    itemList.add(temp);
//                }
//            }
//            listDataChild.put(model, itemList);
//        }
//        adapter.notifyDataSetInvalidated();
    }
}
