package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 8/7/16.
 */
public class HomeResponseModel {

    private String salam;
    private Integer point;
    private Integer balance;
    private String username;

    public String getSalam() {
        return salam;
    }

    public void setSalam(String salam) {
        this.salam = salam;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
