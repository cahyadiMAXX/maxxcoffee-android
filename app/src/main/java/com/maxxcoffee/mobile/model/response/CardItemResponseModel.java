package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class CardItemResponseModel {

    private int id_card;
    private int card_id;
    private int id_user;
    private String card_number;
    private String card_name;
    private String card_image;
    private String barcode;
    private String distribution_id;
    private String card_pin;
    private String status;
    private int beans; // updated from point
    private int balance;
    private int customer_id;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private String activated_date;
    private String confirmed_date;
    private String expired_date;
    private String created_at;
    private String updated_at;


    private int virtual_card;
    private int primary;
    private int cardAmount;

    public int getId_card() {
        return id_card;
    }

    public void setId_card(int id_card) {
        this.id_card = id_card;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }

    public String getCard_pin() {
        return card_pin;
    }

    public void setCard_pin(String card_pin) {
        this.card_pin = card_pin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBeans() {
        return beans;
    }

    public void setBeans(int beans) {
        this.beans = beans;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getActivated_date() {
        return activated_date;
    }

    public void setActivated_date(String activated_date) {
        this.activated_date = activated_date;
    }

    public String getConfirmed_date() {
        return confirmed_date;
    }

    public void setConfirmed_date(String confirmed_date) {
        this.confirmed_date = confirmed_date;
    }

    public String getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(String expired_date) {
        this.expired_date = expired_date;
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

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
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

    @Override
    public String toString() {
        return "CardItemResponseModel{" +
                "id_card=" + id_card +
                ", card_id=" + card_id +
                ", id_user=" + id_user +
                ", card_number='" + card_number + '\'' +
                ", card_name='" + card_name + '\'' +
                ", card_image='" + card_image + '\'' +
                ", barcode='" + barcode + '\'' +
                ", distribution_id='" + distribution_id + '\'' +
                ", card_pin='" + card_pin + '\'' +
                ", status='" + status + '\'' +
                ", beans=" + beans +
                ", balance=" + balance +
                ", customer_id=" + customer_id +
                ", customer_name='" + customer_name + '\'' +
                ", customer_email='" + customer_email + '\'' +
                ", customer_phone='" + customer_phone + '\'' +
                ", activated_date='" + activated_date + '\'' +
                ", confirmed_date='" + confirmed_date + '\'' +
                ", expired_date='" + expired_date + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", primary=" + primary +
                '}';
    }
}
