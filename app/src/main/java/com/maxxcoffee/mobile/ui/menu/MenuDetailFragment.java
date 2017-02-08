package com.maxxcoffee.mobile.ui.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.FormActivity;
import com.maxxcoffee.mobile.adapter.MenuDetailAdapter;
import com.maxxcoffee.mobile.database.controller.MenuController;
import com.maxxcoffee.mobile.database.entity.MenuEntity;
import com.maxxcoffee.mobile.model.MenuPriceModel;
import com.maxxcoffee.mobile.model.response.VarianResponseModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rioswarawan on 7/16/16.
 */
public class MenuDetailFragment extends Fragment {

    @Bind(R.id.backdrop)
    ImageView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.hot_layout)
    LinearLayout hotLayout;
    @Bind(R.id.list_hot)
    ListView hotList;

    @Bind(R.id.iced_layout)
    LinearLayout iceLayout;
    @Bind(R.id.list_iced)
    ListView icedList;

    @Bind(R.id.none_layout)
    LinearLayout noneLayout;
    @Bind(R.id.price)
    TextView price;

    private FormActivity activity;
    private MenuController controller;
    private MenuDetailAdapter iceAdapter;
    private MenuDetailAdapter hotAdapter;
    private List<MenuPriceModel> iceData;
    private List<MenuPriceModel> hotData;

    public MenuDetailFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FormActivity) getActivity();

        controller = new MenuController(activity);
        hotData = new ArrayList<>();
        iceData = new ArrayList<>();
        hotAdapter = new MenuDetailAdapter(activity, hotData);
        iceAdapter = new MenuDetailAdapter(activity, iceData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_detail, container, false);

        ButterKnife.bind(this, view);

        hotList.setAdapter(hotAdapter);
        icedList.setAdapter(iceAdapter);

        fetchingData();
        return view;
    }

    private void fetchingData() {
        Integer mId = getArguments().getInt("menu-id", -1);
        String mTitle = getArguments().getString("title");

        activity.setTitle(mTitle);
        MenuEntity menu = controller.getMenuItem(mId);
        if (menu != null) {
            String jsonHot = menu.getPrice_hot();
            String jsonIced = menu.getPrice_iced();
            String jsonNone = menu.getPrice_none();

            Glide.with(this).load(menu.getImage()).placeholder(getResources().getDrawable(R.drawable.ic_no_image)).crossFade().into(image);
            name.setText(menu.getName());

            if (!jsonHot.equals("")) {
                hotLayout.setVisibility(View.VISIBLE);
                List<VarianResponseModel.VarianItem> listHot = new Gson().fromJson(jsonHot, new TypeToken<List<VarianResponseModel.VarianItem>>() {
                }.getType());

                for (VarianResponseModel.VarianItem item : listHot) {
                    MenuPriceModel model = new MenuPriceModel();
                    model.setSize(getSizeString(item.getSize()));
                    model.setPrice("IDR " + item.getPrice());

                    hotData.add(model);
                }
                hotAdapter.notifyDataSetInvalidated();
            }
            if (!jsonIced.equals("")) {
                iceLayout.setVisibility(View.VISIBLE);
                List<VarianResponseModel.VarianItem> listIce = new Gson().fromJson(jsonIced, new TypeToken<List<VarianResponseModel.VarianItem>>() {
                }.getType());

                for (VarianResponseModel.VarianItem item : listIce) {
                    MenuPriceModel model = new MenuPriceModel();
                    model.setSize(getSizeString(item.getSize()));
                    model.setPrice("IDR " + item.getPrice());

                    iceData.add(model);
                }
                iceAdapter.notifyDataSetInvalidated();
            }
            if (!jsonNone.equals("")) {
                noneLayout.setVisibility(View.VISIBLE);
                List<VarianResponseModel.VarianItem> listNone = new Gson().fromJson(jsonNone, new TypeToken<List<VarianResponseModel.VarianItem>>() {
                }.getType());

                for (VarianResponseModel.VarianItem item : listNone) {
                    price.setText("IDR " + item.getPrice());
                }
            }
        }
    }

    private String getSizeString(String size) {
        if (size.equalsIgnoreCase("S"))
            return "Small";
        if (size.equalsIgnoreCase("M"))
            return "Medium";
        if (size.equalsIgnoreCase("R"))
            return "Regular";
        if (size.equalsIgnoreCase("MX"))
            return "MAXX";
        else
            return "Not Defined";
    }
}
