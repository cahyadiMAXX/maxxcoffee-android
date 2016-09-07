package com.maxxcoffee.mobile.model;

/**
 * Created by Rio Swarawan on 5/29/2016.
 */
public class CardModel {

    private Integer id;
    private String name;
    private String number;
    private String image;
    private String distribution_id;
    private String card_pin;
    private Integer balance;
    private Integer point;
    private String expired_date;

    private int primary;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }

    public String getCard_pin() {
        return card_pin;
    }

    public void setCard_pin(String card_pin) {
        this.card_pin = card_pin;
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

    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return "CardModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", image='" + image + '\'' +
                ", distribution_id='" + distribution_id + '\'' +
                ", card_pin='" + card_pin + '\'' +
                ", balance=" + balance +
                ", point=" + point +
                ", expired_date='" + expired_date + '\'' +
                '}';
    }
}
