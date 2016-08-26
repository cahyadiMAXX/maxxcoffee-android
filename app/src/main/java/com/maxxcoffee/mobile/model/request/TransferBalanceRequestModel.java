package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/24/16.
 */
public class TransferBalanceRequestModel extends DefaultRequestModel{

    private String source_dist_id;
    private String destinaton_dist_id;

    public String getSource_dist_id() {
        return source_dist_id;
    }

    public void setSource_dist_id(String source_dist_id) {
        this.source_dist_id = source_dist_id;
    }

    public String getDestination_dist_id() {
        return destinaton_dist_id;
    }

    public void setDestination_dist_id(String destination_dist_id) {
        this.destinaton_dist_id = destination_dist_id;
    }
}
