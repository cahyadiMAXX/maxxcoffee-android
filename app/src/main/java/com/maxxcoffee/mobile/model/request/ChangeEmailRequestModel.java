package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 8/4/16.
 */
public class ChangeEmailRequestModel {
    String token;
    String new_email;
    String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNew_email() {
        return new_email;
    }

    public void setNew_email(String new_email) {
        this.new_email = new_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
