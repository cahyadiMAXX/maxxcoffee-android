package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.EventEntity;
import com.maxxcoffee.mobile.database.entity.PromoEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class EventController {
    private DatabaseConfig database;

    public EventController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(EventEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(EventEntity entity) {
        try {
            database.getEventDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EventEntity> getEvents() {
        List<EventEntity> data = new ArrayList<>();
        try {
            Dao<EventEntity, Integer> dao = database.getEventDao();
            QueryBuilder<EventEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
