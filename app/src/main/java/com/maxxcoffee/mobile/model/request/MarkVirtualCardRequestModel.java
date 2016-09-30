package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class MarkVirtualCardRequestModel {
    private String token;
    private String distribution_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }
}
