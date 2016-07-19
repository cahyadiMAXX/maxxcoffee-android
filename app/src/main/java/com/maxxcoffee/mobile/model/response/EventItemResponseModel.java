package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class EventItemResponseModel {

    private int id_event;
    private int id_store;
    private String nama_store;
    private String nama_event;
    private String location;
    private String tanggal_start;
    private String tanggal_end;
    private String waktu_start;
    private String waktu_end;
    private String deskripsi;
    private String gambar;
    private String ls_gambar;

    public int getId_store() {
        return id_store;
    }

    public void setId_store(int id_store) {
        this.id_store = id_store;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getNama_store() {
        return nama_store;
    }

    public void setNama_store(String nama_store) {
        this.nama_store = nama_store;
    }

    public String getNama_event() {
        return nama_event;
    }

    public void setNama_event(String nama_event) {
        this.nama_event = nama_event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
