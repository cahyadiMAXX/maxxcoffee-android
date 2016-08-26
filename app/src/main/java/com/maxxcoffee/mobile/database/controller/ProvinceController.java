package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.ProvinceEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class ProvinceController {
    private DatabaseConfig database;

    public ProvinceController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(ProvinceEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(ProvinceEntity entity) {
        try {
            database.getProvinceDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProvinceEntity> getCity() {
        List<ProvinceEntity> data = new ArrayList<>();
        try {
            Dao<ProvinceEntity, Integer> dao = database.getProvinceDao();
            QueryBuilder<ProvinceEntity, Integer> query = dao.queryBuilder();
            query.orderBy("name", true);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<ProvinceEntity> getCities() {
        List<ProvinceEntity> data = new ArrayList<>();
        try {
            Dao<ProvinceEntity, Integer> dao = database.getProvinceDao();
            QueryBuilder<ProvinceEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void clear() {
        database.clearTable(ProvinceEntity.class);
    }
}
