package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.StoreItemEntity;
import com.maxxcoffee.mobile.model.StoreModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class StoreItemController {
    private DatabaseConfig database;

    public StoreItemController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(StoreModel model) {
        StoreItemEntity entity = null;
        if (model.getId() == null) {
            entity = new StoreItemEntity();
        } else {
            entity = getStoreItems(model.getId());
            if (entity == null) {
                entity = new StoreItemEntity();
                entity.setId(model.getId());
            }
        }

        entity.setName(model.getName());
        entity.setAddress(model.getAddress());
        entity.setStoreId(model.getStoreId());
        entity.setContact(model.getContact());
        entity.setOpen(model.getOpen());

        insertOrUpdate(entity);
    }

    private void insertOrUpdate(StoreItemEntity entity) {
        try {
            database.getStoreItemDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StoreItemEntity> getStoreItems() {
        List<StoreItemEntity> data = new ArrayList<>();
        try {
            Dao<StoreItemEntity, Integer> dao = database.getStoreItemDao();
            QueryBuilder<StoreItemEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public StoreItemEntity getStoreItems(Integer id) {
        List<StoreItemEntity> data = new ArrayList<>();
        try {
            Dao<StoreItemEntity, Integer> dao = database.getStoreItemDao();
            QueryBuilder<StoreItemEntity, Integer> query = dao.queryBuilder();
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

    public List<StoreItemEntity> getStoreItemByStore(Integer storeId) {
        List<StoreItemEntity> data = new ArrayList<>();
        try {
            Dao<StoreItemEntity, Integer> dao = database.getStoreItemDao();
            QueryBuilder<StoreItemEntity, Integer> query = dao.queryBuilder();
            query.where().eq("storeId", storeId);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
