package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by rioswarawan on 7/16/16.
 */

@DatabaseTable(tableName = "event")
public class EventEntity {

    @DatabaseField(id = true)
    private int id_event;

    @DatabaseField
    private String nama_event;

    @DatabaseField
    private String nama_lokasi;

    @DatabaseField
    private String alamat_lokasi;

    @DatabaseField
    private String no_telp;

    @DatabaseField
    private String latitude;

    @DatabaseField
    private String longitude;

    @DatabaseField
    private String tanggal_start;

    @DatabaseField
    private String tanggal_end;

    @DatabaseField
    private String waktu_start;

    @DatabaseField
    private String waktu_end;

    @DatabaseField
    private String deskripsi;

    @DatabaseField
    private String gambar;

    @DatabaseField
    private String ls_gambar;

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getNama_event() {
        return nama_event;
    }

    public void setNama_event(String nama_event) {
        this.nama_event = nama_event;
    }

    public String getNama_lokasi() {
        return nama_lokasi;
    }

    public void setNama_lokasi(String nama_lokasi) {
        this.nama_lokasi = nama_lokasi;
    }

    public String getAlamat_lokasi() {
        return alamat_lokasi;
    }

    public void setAlamat_lokasi(String alamat_lokasi) {
        this.alamat_lokasi = alamat_lokasi;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
