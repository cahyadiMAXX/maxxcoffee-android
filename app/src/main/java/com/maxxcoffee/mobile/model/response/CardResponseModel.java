package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class CardResponseModel {

    private String status;
    private String access_token;
    private String messages;
    private Input input;
    private User user;
    private List<CardItemResponseModel> result;

    public List<CardItemResponseModel> getResult() {
        return result;
    }

    public void setResult(List<CardItemResponseModel> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class Input {
        private String cardName;
        private String cardNo;
        private String token;

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public class User {
        private Integer id_user;
        private String nama_user;
        private String no_hp;
        private String password;

        public Integer getId_user() {
            return id_user;
        }

        public void setId_user(Integer id_user) {
            this.id_user = id_user;
        }

        public String getNama_user() {
            return nama_user;
        }

        public void setNama_user(String nama_user) {
            this.nama_user = nama_user;
        }

        public String getNo_hp() {
            return no_hp;
        }

        public void setNo_hp(String no_hp) {
            this.no_hp = no_hp;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Override
    public String toString() {
        return "CardResponseModel{" +
                "status='" + status + '\'' +
                ", access_token='" + access_token + '\'' +
                ", messages='" + messages + '\'' +
                ", input=" + input +
                ", user=" + user +
                ", result=" + result +
                '}';
    }
}
