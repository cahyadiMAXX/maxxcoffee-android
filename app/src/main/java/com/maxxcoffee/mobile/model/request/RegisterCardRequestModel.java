package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/21/16.
 */
public class RegisterCardRequestModel extends DefaultRequestModel {

    private String cardNo;
    private String cardName;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
}
