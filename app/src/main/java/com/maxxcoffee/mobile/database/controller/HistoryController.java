package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.HistoryEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class HistoryController {
    private DatabaseConfig database;

    public HistoryController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(HistoryEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(HistoryEntity entity) {
        try {
            database.getHistoryDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HistoryEntity getHistory(String id) {
        List<HistoryEntity> data = new ArrayList<>();
        try {
            Dao<HistoryEntity, Integer> dao = database.getHistoryDao();
            QueryBuilder<HistoryEntity, Integer> query = dao.queryBuilder();
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

    public List<HistoryEntity> getHistoryByType(String type) {
        List<HistoryEntity> data = new ArrayList<>();
        try {
            Dao<HistoryEntity, Integer> dao = database.getHistoryDao();
            QueryBuilder<HistoryEntity, Integer> query = dao.queryBuilder();
            query.where().eq("type", type);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<HistoryEntity> getHistoryByType(String... type) {
        List<HistoryEntity> data = new ArrayList<>();
        try {
            Dao<HistoryEntity, Integer> dao = database.getHistoryDao();
            QueryBuilder<HistoryEntity, Integer> query = dao.queryBuilder();
            query.where().eq("type", type[0]).or().eq("type", type[1]);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<HistoryEntity> getCards() {
        List<HistoryEntity> data = new ArrayList<>();
        try {
            Dao<HistoryEntity, Integer> dao = database.getHistoryDao();
            QueryBuilder<HistoryEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void clear() {
        database.clearTable(HistoryEntity.class);
    }
}
