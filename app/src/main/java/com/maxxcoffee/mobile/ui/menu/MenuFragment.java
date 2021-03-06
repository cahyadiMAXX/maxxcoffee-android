package com.maxxcoffee.mobile.ui.menu;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.MainActivity;
import com.maxxcoffee.mobile.adapter.MenuPagerAdapter;
import com.maxxcoffee.mobile.database.controller.MenuCategoryController;
import com.maxxcoffee.mobile.database.controller.MenuController;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;
import com.maxxcoffee.mobile.model.response.MenuItemResponseModel;
import com.maxxcoffee.mobile.task.menu.MenuTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    public MenuFragment(){}

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

        if(!Utils.isConnected(activity)){
            Toast.makeText(activity, activity.getResources().getString(R.string.mobile_data), Toast.LENGTH_LONG).show();
            tabs.post(new Runnable() {
                @Override
                public void run() {
                    setupViewPager(viewPager);
                    tabs.setupWithViewPager(viewPager);
                }
            });
        }else{
            fetchingData();
        }

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MenuPagerAdapter adapter = new MenuPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void fetchingData() {
        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        MenuTask task = new MenuTask(activity) {
            @Override
            public void onSuccess(Map<String, List<MenuItemResponseModel>> responseModel) {
                try {
                    if (responseModel != null) {
                        menuController.clearTable();

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
                } catch (Exception e){
                    e.printStackTrace();
                }

                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }
}
