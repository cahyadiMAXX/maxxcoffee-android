package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.model.MenuCategoryModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MenuCategoryController {
    private DatabaseConfig database;

    public MenuCategoryController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(MenuCategoryModel model) {
        MenuCategoryEntity entity = null;
        if (model.getId() == null) {
            entity = new MenuCategoryEntity();
        } else {
            entity = getMenuCategory(model.getId());
            if (entity == null) {
                entity = new MenuCategoryEntity();
                entity.setId(model.getId());
            }
        }
        entity.setName(model.getName());

        insertOrUpdate(entity);
    }

    public void insertOrUpdate(MenuCategoryEntity entity) {
        try {
            database.getMenuCategoryDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MenuCategoryEntity getMenuCategory(Integer id) {
        List<MenuCategoryEntity> data = new ArrayList<>();
        try {
            Dao<MenuCategoryEntity, Integer> dao = database.getMenuCategoryDao();
            QueryBuilder<MenuCategoryEntity, Integer> query = dao.queryBuilder();
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

    public List<MenuCategoryEntity> getMenuCategories() {
        List<MenuCategoryEntity> data = new ArrayList<>();
        try {
            Dao<MenuCategoryEntity, Integer> dao = database.getMenuCategoryDao();
            QueryBuilder<MenuCategoryEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
