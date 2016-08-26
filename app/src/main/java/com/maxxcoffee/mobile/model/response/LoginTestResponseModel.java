package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 8/11/16.
 */
public class LoginTestResponseModel {

    private String status;
    private String email;
    private String password;
    private String phone_number;
    private String ganti_nomer;
    private String ganti_occupation;
    private String messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGanti_nomer() {
        return ganti_nomer;
    }

    public void setGanti_nomer(String ganti_nomer) {
        this.ganti_nomer = ganti_nomer;
    }

    public String getGanti_occupation() {
        return ganti_occupation;
    }

    public void setGanti_occupation(String ganti_occupation) {
        this.ganti_occupation = ganti_occupation;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
