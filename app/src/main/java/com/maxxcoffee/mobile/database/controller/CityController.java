package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.CityEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class CityController {
    private DatabaseConfig database;

    public CityController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(CityEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(CityEntity entity) {
        try {
            database.getCityDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CityEntity> getAllCity() {
        List<CityEntity> data = new ArrayList<>();
        try {
            Dao<CityEntity, Integer> dao = database.getCityDao();
            QueryBuilder<CityEntity, Integer> query = dao.queryBuilder();
            query.orderBy("name", true);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void clear() {
        database.clearTable(CityEntity.class);
    }
}
