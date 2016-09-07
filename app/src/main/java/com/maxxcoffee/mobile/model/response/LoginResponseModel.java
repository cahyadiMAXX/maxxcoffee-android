package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class LoginResponseModel {

    private String status;
    private String token;
    private String username;
    private String mobile_phone_user;
    private String level_akses;
    private String foto;
    private String balance_total;
    private String beans;

    private String messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile_phone_user() {
        return mobile_phone_user;
    }

    public void setMobile_phone_user(String mobile_phone_user) {
        this.mobile_phone_user = mobile_phone_user;
    }

    public String getLevel_akses() {
        return level_akses;
    }

    public void setLevel_akses(String level_akses) {
        this.level_akses = level_akses;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getBalance_total() {
        return balance_total;
    }

    public void setBalance_total(String balance_total) {
        this.balance_total = balance_total;
    }

    public String getBeans() {
        return beans;
    }

    public void setBeans(String beans) {
        this.beans = beans;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
