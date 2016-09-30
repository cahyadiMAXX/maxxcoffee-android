package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class GCMRequestModel {

    private String token;

    private String email;
    private String device_id;
    private String device_token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
