package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class LoginRequestModel {

    private String email;
    private String password;

    //tambah
    private String device_id;
    private String gadget_id;
    private String device_token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getGadget_id() {
        return gadget_id;
    }

    public void setGadget_id(String gadget_id) {
        this.gadget_id = gadget_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
