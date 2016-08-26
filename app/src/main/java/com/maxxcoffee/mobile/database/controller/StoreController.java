package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.StoreEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class StoreController {
    private DatabaseConfig database;

    public StoreController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

//    public void insert(StoreEntity model) {
//        StoreEntity entity = null;
//        if (model.getId() == null) {
//            entity = new StoreEntity();
//        } else {
//            entity = getStore(model.getId());
//            if (entity == null) {
//                entity = new StoreEntity();
//                entity.setId(model.getId());
//            }
//        }
//
//        entity.setName(model.getName());
//        entity.setAddress(model.getAddress());
//        entity.setCity(model.getCity());
//        entity.setProvince(model.getProvince());
//        entity.setIsland(model.getIsland());
//        entity.setZipcode(model.getZipcode());
//        entity.setLatitude(model.getLatitude());
//        entity.setLongitude(model.getLongitude());
//        entity.setOpen(model.getOpen());
//        entity.setClose(model.getClose());
//        entity.setPhone(model.getPhone());
//        entity.setFeature(model.getFeature());
//        entity.setFeature_icon(model.getFeature_icon());
//
//        insertOrUpdate(entity);
//    }

    public void insert(StoreEntity model) {
        insertOrUpdate(model);
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

    public List<StoreEntity> getStores(String province) {
        List<StoreEntity> data = new ArrayList<>();
        try {
            Dao<StoreEntity, Integer> dao = database.getStoreDao();
            QueryBuilder<StoreEntity, Integer> query = dao.queryBuilder();
            query.where().eq("province", province);
            query.orderBy("province", true);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<StoreEntity> getStoreProvices() {
        List<StoreEntity> data = new ArrayList<>();
        try {
            Dao<StoreEntity, Integer> dao = database.getStoreDao();
            QueryBuilder<StoreEntity, Integer> query = dao.queryBuilder();
            query.distinct().selectColumns("province");
            query.where().not().eq("province", "");

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
