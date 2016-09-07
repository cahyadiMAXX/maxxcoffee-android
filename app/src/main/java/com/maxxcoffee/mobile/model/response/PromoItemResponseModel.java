package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class PromoItemResponseModel {

    private int id_promo;
    private String nama_promo;
    private String deskripsi;
    private String gambar;
    private String ls_gambar;
    private String syarat;
    private String tanggal_start;
    private String tanggal_end;
    private String waktu_start;
    private String waktu_end;
    private String created_at;

    public int getId_promo() {
        return id_promo;
    }

    public void setId_promo(int id_promo) {
        this.id_promo = id_promo;
    }

    public String getNama_promo() {
        return nama_promo;
    }

    public void setNama_promo(String nama_promo) {
        this.nama_promo = nama_promo;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getLs_gambar() {
        return ls_gambar;
    }

    public void setLs_gambar(String ls_gambar) {
        this.ls_gambar = ls_gambar;
    }

    public String getSyarat() {
        return syarat;
    }

    public void setSyarat(String syarat) {
        this.syarat = syarat;
    }

    public String getTanggal_start() {
        return tanggal_start;
    }

    public void setTanggal_start(String tanggal_start) {
        this.tanggal_start = tanggal_start;
    }

    public String getTanggal_end() {
        return tanggal_end;
    }

    public void setTanggal_end(String tanggal_end) {
        this.tanggal_end = tanggal_end;
    }

    public String getWaktu_start() {
        return waktu_start;
    }

    public void setWaktu_start(String waktu_start) {
        this.waktu_start = waktu_start;
    }

    public String getWaktu_end() {
        return waktu_end;
    }

    public void setWaktu_end(String waktu_end) {
        this.waktu_end = waktu_end;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "PromoItemResponseModel{" +
                "id_promo=" + id_promo +
                ", nama_promo='" + nama_promo + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", gambar='" + gambar + '\'' +
                ", ls_gambar='" + ls_gambar + '\'' +
                ", syarat='" + syarat + '\'' +
                ", tanggal_start='" + tanggal_start + '\'' +
                ", tanggal_end='" + tanggal_end + '\'' +
                ", waktu_start='" + waktu_start + '\'' +
                ", waktu_end='" + waktu_end + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
