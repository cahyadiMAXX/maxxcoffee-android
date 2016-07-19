package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class StoreItemResponseModel {
    private int id_store;
    private String nama_store;
    private String alamat_store;
    private String kota_store;
    private String provinsi_store;
    private String pulau;
    private String kodepos_store;
    private String latitude;
    private String longitude;
    private String jam_buka;
    private String jam_tutup;
    private String phone_store;
    private List<String> foto;
    private List<String> feature;
    private List<String> icon_feature;

    public int getId_store() {
        return id_store;
    }

    public void setId_store(int id_store) {
        this.id_store = id_store;
    }

    public String getNama_store() {
        return nama_store;
    }

    public void setNama_store(String nama_store) {
        this.nama_store = nama_store;
    }

    public String getAlamat_store() {
        return alamat_store;
    }

    public void setAlamat_store(String alamat_store) {
        this.alamat_store = alamat_store;
    }

    public String getKota_store() {
        return kota_store;
    }

    public void setKota_store(String kota_store) {
        this.kota_store = kota_store;
    }

    public String getProvinsi_store() {
        return provinsi_store;
    }

    public void setProvinsi_store(String provinsi_store) {
        this.provinsi_store = provinsi_store;
    }

    public String getPulau() {
        return pulau;
    }

    public void setPulau(String pulau) {
        this.pulau = pulau;
    }

    public String getKodepos_store() {
        return kodepos_store;
    }

    public void setKodepos_store(String kodepos_store) {
        this.kodepos_store = kodepos_store;
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

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
    }

    public String getPhone_store() {
        return phone_store;
    }

    public void setPhone_store(String phone_store) {
        this.phone_store = phone_store;
    }

    public List<String> getFoto() {
        return foto;
    }

    public void setFoto(List<String> foto) {
        this.foto = foto;
    }

    public List<String> getFeature() {
        return feature;
    }

    public void setFeature(List<String> feature) {
        this.feature = feature;
    }

    public List<String> getIcon_feature() {
        return icon_feature;
    }

    public void setIcon_feature(List<String> icon_feature) {
        this.icon_feature = icon_feature;
    }
}
