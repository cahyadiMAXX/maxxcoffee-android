package com.maxxcoffee.mobile.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.MenuPagerAdapter;
import com.maxxcoffee.mobile.adapter.StorePagerAdapter;
import com.maxxcoffee.mobile.api.ApiManager;
import com.maxxcoffee.mobile.database.controller.MenuCategoryController;
import com.maxxcoffee.mobile.database.controller.MenuController;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;
import com.maxxcoffee.mobile.model.response.MenuItemResponseModel;
import com.maxxcoffee.mobile.task.MenuTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.widget.TBaseProgress;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class MenuFragment extends Fragment {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private MainActivity activity;
    private String token;
    private MenuController menuController;
    private MenuCategoryController categoryController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(false);
        token = PreferenceManager.getString(activity, Constant.PREFERENCE_TOKEN, "");

        menuController = new MenuController(activity);
        categoryController = new MenuCategoryController(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);
        activity.setTitle("Menu");

        fetchingData();
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MenuPagerAdapter adapter = new MenuPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void fetchingData() {
        final TBaseProgress progress = new TBaseProgress(activity);
        progress.show();

        MenuTask task = new MenuTask(activity) {
            @Override
            public void onSuccess(Map<String, List<MenuItemResponseModel>> responseModel) {
                if (responseModel != null) {
                    for (Map.Entry<String, List<MenuItemResponseModel>> entry : responseModel.entrySet()) {
                        String key = entry.getKey();
                        List<MenuItemResponseModel> valueList = entry.getValue();

                        for (MenuItemResponseModel menu : valueList) {
                            String jsonPriceHot = "";
                            String jsonPriceIced = "";
                            String jsonPriceNone = "";

                            if (menu.getVarian().getHot().size() > 0)
                                jsonPriceHot = new Gson().toJson(menu.getVarian().getHot());
                            if (menu.getVarian().getIced().size() > 0)
                                jsonPriceIced = new Gson().toJson(menu.getVarian().getIced());
                            if (menu.getVarian().getNone().size() > 0)
                                jsonPriceNone = new Gson().toJson(menu.getVarian().getNone());

                            MenuEntity entity = new MenuEntity();
                            entity.setId(menu.getId_Menu());
                            entity.setName(menu.getNama_menu());
                            entity.setAvailable_size(menu.getAvailable_size());
                            entity.setAvailable_type(menu.getAvailable_type());
                            entity.setDescription(menu.getDeskripsi());
                            entity.setImage(menu.getGambar());
                            entity.setRedeem_point(menu.getRedeem_point());
                            entity.setStatus(menu.getStatus());
                            entity.setGroup(menu.getGroup());
                            entity.setCategory_id(menu.getId_kategori());
                            entity.setCategory(menu.getKategori());
                            entity.setTags(menu.getTags());
                            entity.setPrice_hot(jsonPriceHot);
                            entity.setPrice_iced(jsonPriceIced);
                            entity.setPrice_none(jsonPriceNone);
                            menuController.insert(entity);

                            MenuCategoryEntity category = new MenuCategoryEntity();
                            category.setId(menu.getId_kategori());
                            category.setCategory(menu.getKategori());
                            category.setGroup(menu.getGroup());
                            categoryController.insert(category);
                        }
                    }

                    tabs.post(new Runnable() {
                        @Override
                        public void run() {
                            setupViewPager(viewPager);
                            tabs.setupWithViewPager(viewPager);
                        }
                    });
                }

                if (progress.isShowing())
                    progress.dismiss();
            }

            @Override
            public void onFailed() {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity, "Failed to fetching data.", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }
}
