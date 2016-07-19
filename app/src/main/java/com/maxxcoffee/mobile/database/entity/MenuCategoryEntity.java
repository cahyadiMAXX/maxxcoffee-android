package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rioswarawan on 7/15/16.
 */

@DatabaseTable(tableName = "menu_category")
public class MenuCategoryEntity {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String category;

    @DatabaseField
    private String group;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
