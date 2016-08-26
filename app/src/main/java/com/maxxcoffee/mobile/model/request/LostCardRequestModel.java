package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/21/16.
 */
public class LostCardRequestModel extends DefaultRequestModel {
    private String id_card;
    private String subject;
    private String detail;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
