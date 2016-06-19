package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.MenuItemEntity;
import com.maxxcoffee.mobile.database.entity.MenuItemEntity;
import com.maxxcoffee.mobile.model.MenuCategoryModel;
import com.maxxcoffee.mobile.model.MenuItemModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MenuItemController {
    private DatabaseConfig database;

    public MenuItemController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(MenuItemModel model) {
        MenuItemEntity entity = null;
        if (model.getId() == null) {
            entity = new MenuItemEntity();
        } else {
            entity = getMenuItem(model.getId());
            if (entity == null) {
                entity = new MenuItemEntity();
                entity.setId(model.getId());
            }
        }
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setImage(model.getImage());
        entity.setPrice(model.getPrice());
        entity.setPoint(model.getPoint());
        entity.setCategoryId(model.getCategoryId());

        insertOrUpdate(entity);
    }

    public void insertOrUpdate(MenuItemEntity entity) {
        try {
            database.getMenuItemDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MenuItemEntity getMenuItem(Integer id) {
        List<MenuItemEntity> data = new ArrayList<>();
        try {
            Dao<MenuItemEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuItemEntity, Integer> query = dao.queryBuilder();
            query.where().eq("id", id);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data.size() > 0) {
            return data.get(0);
        }
        return null;
    }

    public List<MenuItemEntity> getMenuItemByCategory(Integer categoryId) {
        List<MenuItemEntity> data = new ArrayList<>();
        try {
            Dao<MenuItemEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuItemEntity, Integer> query = dao.queryBuilder();
            query.where().eq("categoryId", categoryId);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MenuItemEntity> getMenuItems() {
        List<MenuItemEntity> data = new ArrayList<>();
        try {
            Dao<MenuItemEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuItemEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
