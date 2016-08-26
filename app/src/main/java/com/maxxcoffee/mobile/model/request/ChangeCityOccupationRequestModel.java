package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 8/4/16.
 */
public class ChangeCityOccupationRequestModel {
    String token;
    String occupation;
    String kota_user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getKota_user() {
        return kota_user;
    }

    public void setKota_user(String kota_user) {
        this.kota_user = kota_user;
    }
}
