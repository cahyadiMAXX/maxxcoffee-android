package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 8/4/16.
 */
public class RenameCardRequestModel {
    String token;
    String id_card;
    String new_name;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getNew_name() {
        return new_name;
    }

    public void setNew_name(String new_name) {
        this.new_name = new_name;
    }
}
