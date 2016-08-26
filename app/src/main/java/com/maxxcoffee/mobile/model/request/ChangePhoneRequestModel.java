package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/25/16.
 */
public class ChangePhoneRequestModel extends DefaultRequestModel{
    private String email;
    private String newhp;

    private String password;
    private String mobile_phone_user;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewhp() {
        return newhp;
    }

    public void setNewhp(String newhp) {
        this.newhp = newhp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_phone_user() {
        return mobile_phone_user;
    }

    public void setMobile_phone_user(String mobile_phone_user) {
        this.mobile_phone_user = mobile_phone_user;
    }
}
