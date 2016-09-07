package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.CardEntity;
import com.maxxcoffee.mobile.database.entity.CardPrimaryEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class CardPrimaryController {
    private DatabaseConfig database;

    public CardPrimaryController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

    public void insert(CardPrimaryEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(CardPrimaryEntity entity) {
        try {
            database.getCardPrimaryDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CardPrimaryEntity> getCards() {
        List<CardPrimaryEntity> data = new ArrayList<>();
        try {
            Dao<CardPrimaryEntity, Integer> dao = database.getCardPrimaryDao();
            QueryBuilder<CardPrimaryEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void clear() {
        database.clearTable(CardPrimaryEntity.class);
    }
}
