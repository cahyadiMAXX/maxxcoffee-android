package com.maxxcoffee.mobile.model.response;

import java.util.ArrayList;

/**
 * Created by rioswarawan on 7/24/16.
 */
public class CardImageResponseModel extends DefaultResponseModel{

    private ArrayList<String> card_image;

    private ArrayList<String> card_image_url;

    public ArrayList<String> getCard_image() {
        return card_image;
    }

    public void setCard_image(ArrayList<String> card_image) {
        this.card_image = card_image;
    }

    public ArrayList<String> getCard_image_url() {
        return card_image_url;
    }

    public void setCard_image_url(ArrayList<String> card_image_url) {
        this.card_image_url = card_image_url;
    }
}
