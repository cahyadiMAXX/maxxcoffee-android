package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rioswarawan on 7/15/16.
 */
public class MenuCategoryController {

    private DatabaseConfig database;

    public MenuCategoryController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(MenuCategoryEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(MenuCategoryEntity entity) {
        try {
            database.getMenuCategoryDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MenuCategoryEntity> getMenuCategoryDrinks() {
        List<MenuCategoryEntity> data = new ArrayList<>();
        try {
            Dao<MenuCategoryEntity, Integer> dao = database.getMenuCategoryDao();
            QueryBuilder<MenuCategoryEntity, Integer> query = dao.queryBuilder();
            query.where().eq("group", "Drinks");

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MenuCategoryEntity> getMenuCategoryFood() {
        List<MenuCategoryEntity> data = new ArrayList<>();
        try {
            Dao<MenuCategoryEntity, Integer> dao = database.getMenuCategoryDao();
            QueryBuilder<MenuCategoryEntity, Integer> query = dao.queryBuilder();
            query.where().eq("group", "Food");

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MenuCategoryEntity> getMenuCategoryMerchandise() {
        List<MenuCategoryEntity> data = new ArrayList<>();
        try {
            Dao<MenuCategoryEntity, Integer> dao = database.getMenuCategoryDao();
            QueryBuilder<MenuCategoryEntity, Integer> query = dao.queryBuilder();
            query.where().eq("group", "Merchandise");

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}
