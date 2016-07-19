package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class RegisterRequestModel {
    private String nama_user;
    private String email;
    private String password;
    private String kota_user;
    private String mobile_phone_user;
    private String gender;
    private String occupation;
    private String tanggal_lahir;

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

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

    public String getKota_user() {
        return kota_user;
    }

    public void setKota_user(String kota_user) {
        this.kota_user = kota_user;
    }

    public String getMobile_phone_user() {
        return mobile_phone_user;
    }

    public void setMobile_phone_user(String mobile_phone_user) {
        this.mobile_phone_user = mobile_phone_user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }
}
