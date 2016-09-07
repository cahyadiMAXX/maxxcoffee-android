package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/9/16.
 */
public class ProfileResponseModel {

    private String status;
    private String total_point;
    private String total_balance;
    private List<ProfileItemResponseModel> user_profile;
    private List<CardItemResponseModel> cards;

    public String getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(String total_balance) {
        this.total_balance = total_balance;
    }

    public String getTotal_point() {
        return total_point;
    }

    public void setTotal_point(String total_point) {
        this.total_point = total_point;
    }

    public List<ProfileItemResponseModel> getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(List<ProfileItemResponseModel> user_profile) {
        this.user_profile = user_profile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CardItemResponseModel> getCards() {
        return cards;
    }

    public void setCards(List<CardItemResponseModel> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "ProfileResponseModel{" +
                "status='" + status + '\'' +
                ", total_point='" + total_point + '\'' +
                ", total_balance='" + total_balance + '\'' +
                ", user_profile=" + user_profile +
                ", cards=" + cards +
                '}';
    }
}
