package com.maxxcoffee.mobile.model.response;

import java.util.List;
import java.util.Map;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class MenuResponseModel {

    private String status;
    private Map<String, List<MenuItemResponseModel>> menu;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, List<MenuItemResponseModel>> getMenu() {
        return menu;
    }

    public void setMenu(Map<String, List<MenuItemResponseModel>> menu) {
        this.menu = menu;
    }
}
