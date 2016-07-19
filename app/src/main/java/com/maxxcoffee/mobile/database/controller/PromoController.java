package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.PromoEntity;
import com.maxxcoffee.mobile.model.PromoModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class PromoController {
    private DatabaseConfig database;

    public PromoController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(PromoEntity entity) {
//        PromoEntity entity = null;
//        if (model.getId() == null) {
//            entity = new PromoEntity();
//        } else {
//            entity = getPromo(model.getId());
//            if (entity == null) {
//                entity = new PromoEntity();
//                entity.setId(model.getId());
//            }
//        }
//        entity.setImage(model.getImage());
//        entity.setTitle(model.getTitle());
//        entity.setDescription(model.getDescription());

        insertOrUpdate(entity);
    }

    public void insertOrUpdate(PromoEntity entity) {
        try {
            database.getPromoDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PromoEntity getPromo(Integer id) {
        List<PromoEntity> data = new ArrayList<>();
        try {
            Dao<PromoEntity, Integer> dao = database.getPromoDao();
            QueryBuilder<PromoEntity, Integer> query = dao.queryBuilder();
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

    public List<PromoEntity> getPromos() {
        List<PromoEntity> data = new ArrayList<>();
        try {
            Dao<PromoEntity, Integer> dao = database.getPromoDao();
            QueryBuilder<PromoEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
