package com.maxxcoffee.mobile.database.entity;

import com.j256.ormlite.field.DatabaseField;

import java.util.List;

/**
 * Created by rioswarawan on 7/11/16.
 */
public class ProfileEntity {

    @DatabaseField(id = true)
    Integer id;

    @DatabaseField
    String name;

    @DatabaseField
    String email;

    @DatabaseField
    String phone;

    @DatabaseField
    String city;

    @DatabaseField
    String birthday;

    @DatabaseField
    String gender;

    @DatabaseField
    String occupation;

    @DatabaseField
    String image;

    @DatabaseField
    Integer point;

    @DatabaseField
    Integer balance;

    @DatabaseField
    Boolean sms_verified;

    @DatabaseField
    Boolean email_verified;

    @DatabaseField
    String first_name;

    @DatabaseField
    String last_name;

    @DatabaseField
    String user_code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getSms_verified() {
        return sms_verified;
    }

    public void setSms_verified(Boolean sms_verified) {
        this.sms_verified = sms_verified;
    }

    public Boolean getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(Boolean email_verified) {
        this.email_verified = email_verified;
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

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }
}
