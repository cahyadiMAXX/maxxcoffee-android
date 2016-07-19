package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/13/16.
 */
public class MenuItemResponseModel {

    private int id_Menu;
    private String nama_menu;
    private String available_size;
    private String available_type;
    private String deskripsi;
    private String gambar;
    private int redeem_point;
    private String group;
    private String status;
    private String created_at;
    private String updated_at;
    private int id_kategori;
    private String kategori;
    private String tags;
    private VarianResponseModel varian;

    public int getId_Menu() {
        return id_Menu;
    }

    public void setId_Menu(int id_Menu) {
        this.id_Menu = id_Menu;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getAvailable_size() {
        return available_size;
    }

    public void setAvailable_size(String available_size) {
        this.available_size = available_size;
    }

    public String getAvailable_type() {
        return available_type;
    }

    public void setAvailable_type(String available_type) {
        this.available_type = available_type;
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

    public int getRedeem_point() {
        return redeem_point;
    }

    public void setRedeem_point(int redeem_point) {
        this.redeem_point = redeem_point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public VarianResponseModel getVarian() {
        return varian;
    }

    public void setVarian(VarianResponseModel varian) {
        this.varian = varian;
    }
}
