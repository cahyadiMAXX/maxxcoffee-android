package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.model.CardModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class CardController {
    private DatabaseConfig database;

    public CardController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(CardEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(CardEntity entity) {
        try {
            database.getCardDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CardEntity getCardByDist(String id) {
        List<CardEntity> data = new ArrayList<>();
        try {
            Dao<CardEntity, Integer> dao = database.getCardDao();
            QueryBuilder<CardEntity, Integer> query = dao.queryBuilder();
            query.where().eq("distribution_id", id);

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data.size() > 0) {
            return data.get(0);
        }
        return null;
    }
    public CardEntity getCardById(String id) {
        List<CardEntity> data = new ArrayList<>();
        try {
            Dao<CardEntity, Integer> dao = database.getCardDao();
            QueryBuilder<CardEntity, Integer> query = dao.queryBuilder();
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

    public List<CardEntity> getCards() {
        List<CardEntity> data = new ArrayList<>();
        try {
            Dao<CardEntity, Integer> dao = database.getCardDao();
            QueryBuilder<CardEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void clear() {
        database.clearTable(CardEntity.class);
    }
}
