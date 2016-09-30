package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/17/16.
 */
public class AddVirtualResponseModel {

    private String status;
    private String distribution_id;
    private String card_number;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }
}
