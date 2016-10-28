package com.maxxcoffee.mobile.model.request;

/**
 * Created by vourest on 10/19/16.
 */
public class CardDesignRequestModel extends DefaultRequestModel{

    public String card_number;
    public String card_image;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    @Override
    public String toString() {
        return "CardDesignRequestModel{" +
                "card_number=" + card_number +
                ", card_image='" + card_image + '\'' +
                '}';
    }
}
