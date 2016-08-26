package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/31/16.
 */
public class KotaItemResponseModel {

    String nama_kota;
    String nama_provinsi;
    String nama_pulau;

    public String getNama_kota() {
        return nama_kota;
    }

    public void setNama_kota(String nama_kota) {
        this.nama_kota = nama_kota;
    }

    public String getNama_provinsi() {
        return nama_provinsi;
    }

    public void setNama_provinsi(String nama_provinsi) {
        this.nama_provinsi = nama_provinsi;
    }

    public String getNama_pulau() {
        return nama_pulau;
    }

    public void setNama_pulau(String nama_pulau) {
        this.nama_pulau = nama_pulau;
    }
}
