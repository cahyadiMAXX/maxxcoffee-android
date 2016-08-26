package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rio Swarawan on 5/29/2016.
 */

@DatabaseTable(tableName = "city")
public class CityEntity {

    @DatabaseField(id = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}