package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class CardResponseModel {

    private String status;
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
}
