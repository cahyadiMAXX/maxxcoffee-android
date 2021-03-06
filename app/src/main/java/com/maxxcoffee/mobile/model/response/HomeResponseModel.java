package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 8/7/16.
 */
public class HomeResponseModel {

    private String salam;
    private Integer point;
    private Integer balance;
    private String username;
    private CardItemResponseModel primaryCard;

    private String email;
    private String phone;
    private int beans;

    private int cardAmount;
    private int virtual_card;

    private String verifikasi_email;
    private String verifikasi_sms;

    public String getSalam() {
        return salam;
    }

    public void setSalam(String salam) {
        this.salam = salam;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CardItemResponseModel getPrimaryCard() {
        return primaryCard;
    }

    public void setPrimaryCard(CardItemResponseModel primaryCard) {
        this.primaryCard = primaryCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBeans() {
        return beans;
    }

    public void setBeans(int beans) {
        this.beans = beans;
    }

    public int getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(int cardAmount) {
        this.cardAmount = cardAmount;
    }

    public int getVirtual_card() {
        return virtual_card;
    }

    public void setVirtual_card(int virtual_card) {
        this.virtual_card = virtual_card;
    }

    public String getVerifikasi_email() {
        return verifikasi_email;
    }

    public void setVerifikasi_email(String verifikasi_email) {
        this.verifikasi_email = verifikasi_email;
    }

    public String getVerifikasi_sms() {
        return verifikasi_sms;
    }

    public void setVerifikasi_sms(String verifikasi_sms) {
        this.verifikasi_sms = verifikasi_sms;
    }

    @Override
    public String toString() {
        return "HomeResponseModel{" +
                "salam='" + salam + '\'' +
                ", point=" + point +
                ", balance=" + balance +
                ", username='" + username + '\'' +
                ", primaryCard=" + primaryCard +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", beans=" + beans +
                ", cardAmount=" + cardAmount +
                ", virtual_card=" + virtual_card +
                ", verifikasi_email='" + verifikasi_email + '\'' +
                ", verifikasi_sms='" + verifikasi_sms + '\'' +
                '}';
    }
}
