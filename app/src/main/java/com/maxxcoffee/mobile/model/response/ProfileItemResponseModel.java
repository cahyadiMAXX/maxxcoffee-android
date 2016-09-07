package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/9/16.
 */
public class ProfileItemResponseModel {
    private int id_user;
    private String email;
    private String nama_user;
    private String kota_user;
    private String mobile_phone_user;
    private String tanggal_lahir;
    private String gender;
    private String occupation;
    private String verifikasi_sms;
    private String verifikasi_email;
    private String gambar;

    private String first_name;
    private String last_name;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
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

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
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

    public String getVerifikasi_sms() {
        return verifikasi_sms;
    }

    public void setVerifikasi_sms(String verifikasi_sms) {
        this.verifikasi_sms = verifikasi_sms;
    }

    public String getVerifikasi_email() {
        return verifikasi_email;
    }

    public void setVerifikasi_email(String verifikasi_email) {
        this.verifikasi_email = verifikasi_email;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "ProfileItemResponseModel{" +
                "id_user=" + id_user +
                ", email='" + email + '\'' +
                ", nama_user='" + nama_user + '\'' +
                ", kota_user='" + kota_user + '\'' +
                ", mobile_phone_user='" + mobile_phone_user + '\'' +
                ", tanggal_lahir='" + tanggal_lahir + '\'' +
                ", gender='" + gender + '\'' +
                ", occupation='" + occupation + '\'' +
                ", verifikasi_sms='" + verifikasi_sms + '\'' +
                ", verifikasi_email='" + verifikasi_email + '\'' +
                ", gambar='" + gambar + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
