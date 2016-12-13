package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class DefaultRequestModel {

    private String token;

    // change-name
    private String nama_user;
    private String nama_user_last;

    // change-password
    private String old_password;
    private String new_password;

    private String device_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNama_user_last() {
        return nama_user_last;
    }

    public void setNama_user_last(String nama_user_last) {
        this.nama_user_last = nama_user_last;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    @Override
    public String toString() {
        return "DefaultRequestModel{" +
                "token='" + token + '\'' +
                ", nama_user='" + nama_user + '\'' +
                ", nama_user_last='" + nama_user_last + '\'' +
                ", old_password='" + old_password + '\'' +
                ", new_password='" + new_password + '\'' +
                ", device_id='" + device_id + '\'' +
                '}';
    }
}
