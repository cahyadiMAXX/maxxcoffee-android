package com.maxxcoffee.mobile.database.controller;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.database.entity.ProfileEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/24/2016.
 */
public class ProfileController {
    private DatabaseConfig database;

    public ProfileController(Context context) {
        Context context1 = context;
        if (database == null) {
            this.database = new DatabaseConfig(context);
        }
    }

//    public void insert(ProfileModel model) {
//        ProfileEntity entity = new ProfileEntity();
//
//        entity.setId(model.getId());
//        entity.setName(model.getName());
//        entity.setEmail(model.getEmail());
//        entity.setPhone(model.getPhone());
//        entity.setCity(model.getCity());
//        entity.setBirthday(model.getBirthday());
//        entity.setGender(model.getGender());
//        entity.setOccupation(model.getOccupation());
//        entity.setImage(model.getImage());
//        entity.setPoint(model.getPoint());
//        entity.setBalance(model.getBalance());
//
//        insertOrUpdate(entity);
//    }

    public void insert(ProfileEntity entity) {
        insertOrUpdate(entity);
    }

    public void insertOrUpdate(ProfileEntity entity) {
        try {
            database.getProfileDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProfile() {
        database.clearTable(ProfileEntity.class);
    }

    public ProfileEntity getProfile() {
        List<ProfileEntity> data = new ArrayList<>();
        try {
            Dao<ProfileEntity, Integer> dao = database.getProfileDao();
            QueryBuilder<ProfileEntity, Integer> query = dao.queryBuilder();

            data = query.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (data.size() > 0) {
            return data.get(0);
        }
        return null;
    }
}
