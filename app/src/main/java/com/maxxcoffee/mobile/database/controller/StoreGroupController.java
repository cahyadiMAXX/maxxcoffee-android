package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.StoreEntity;
import com.maxxcoffee.mobile.model.StoreGroupModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class StoreGroupController {
    private DatabaseConfig database;

    public StoreGroupController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(StoreGroupModel model) {
        StoreEntity entity = null;
        if (model.getId() == null) {
            entity = new StoreEntity();
        } else {
            entity = getStore(model.getId());
            if (entity == null) {
                entity = new StoreEntity();
                entity.setId(model.getId());
            }
        }

        entity.setLocation(model.getLocation());
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(StoreEntity entity) {
        try {
            database.getStoreDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StoreEntity getStore(Integer id) {
        List<StoreEntity> data = new ArrayList<>();
        try {
            Dao<StoreEntity, Integer> dao = database.getStoreDao();
            QueryBuilder<StoreEntity, Integer> query = dao.queryBuilder();
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

    public List<StoreEntity> getStores() {
        List<StoreEntity> data = new ArrayList<>();
        try {
            Dao<StoreEntity, Integer> dao = database.getStoreDao();
            QueryBuilder<StoreEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
