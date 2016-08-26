package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.FaqEntity;
import com.maxxcoffee.mobile.model.FaqModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class FaqController {
    private DatabaseConfig database;

    public FaqController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(FaqEntity model) {
        insertOrUpdate(model);
    }

    public void insertOrUpdate(FaqEntity entity) {
        try {
            database.getFaqDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FaqEntity getFaq(Integer id) {
        List<FaqEntity> data = new ArrayList<>();
        try {
            Dao<FaqEntity, Integer> dao = database.getFaqDao();
            QueryBuilder<FaqEntity, Integer> query = dao.queryBuilder();
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

    public List<FaqEntity> getFaqs() {
        List<FaqEntity> data = new ArrayList<>();
        try {
            Dao<FaqEntity, Integer> dao = database.getFaqDao();
            QueryBuilder<FaqEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
