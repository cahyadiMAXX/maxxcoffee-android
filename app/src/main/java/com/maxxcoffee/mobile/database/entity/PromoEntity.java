package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rio Swarawan on 5/10/2016.
 */

@DatabaseTable(tableName = "promo")
public class PromoEntity {

    @DatabaseField(id = true)
    Integer id;

    @DatabaseField
    Integer image;

    @DatabaseField
    String title;

    @DatabaseField
    String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
