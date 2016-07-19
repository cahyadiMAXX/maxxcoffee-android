package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.MenuEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class MenuController {
    private DatabaseConfig database;

    public MenuController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(MenuEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(MenuEntity entity) {
        try {
            database.getMenuItemDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MenuEntity getMenuItem(Integer id) {
        List<MenuEntity> data = new ArrayList<>();
        try {
            Dao<MenuEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuEntity, Integer> query = dao.queryBuilder();
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

    public List<MenuEntity> getMenuItemByCategory(Integer categoryId) {
        List<MenuEntity> data = new ArrayList<>();
        try {
            Dao<MenuEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuEntity, Integer> query = dao.queryBuilder();
            query.where().eq("category_id", categoryId);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MenuEntity> getMenuItems() {
        List<MenuEntity> data = new ArrayList<>();
        try {
            Dao<MenuEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MenuEntity> getMenuDrink() {
        List<MenuEntity> data = new ArrayList<>();
        try {
            Dao<MenuEntity, Integer> dao = database.getMenuItemDao();
            QueryBuilder<MenuEntity, Integer> query = dao.queryBuilder();
                query.where().eq("group", "Drinks");

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
