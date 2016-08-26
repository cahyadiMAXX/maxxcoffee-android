package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/28/16.
 */
public class DeleteCardRequestModel extends DefaultRequestModel{

    private String card_number;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }
}
