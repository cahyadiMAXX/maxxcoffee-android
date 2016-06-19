package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rio Swarawan on 5/29/2016.
 */

@DatabaseTable(tableName = "card")
public class CardEntity {

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField
    private Integer balance;

    @DatabaseField
    private Integer point;

    @DatabaseField
    private Integer beans;

    @DatabaseField
    private String expDate;

    @DatabaseField
    private Integer image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getBeans() {
        return beans;
    }

    public void setBeans(Integer beans) {
        this.beans = beans;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
