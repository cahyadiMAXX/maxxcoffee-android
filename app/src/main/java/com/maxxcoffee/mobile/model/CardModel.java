package com.maxxcoffee.mobile.model;

/**
 * Created by Rio Swarawan on 5/29/2016.
 */
public class CardModel {

    private Integer id;
    private String name;
    private Integer balance;
    private Integer point;
    private Integer beans;
    private String expDate;
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
