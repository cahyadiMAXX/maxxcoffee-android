package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class PromoResponseModel {

    private String status;
    private List<PromoItemResponseModel> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PromoItemResponseModel> getResult() {
        return result;
    }

    public void setResult(List<PromoItemResponseModel> result) {
        this.result = result;
    }
}
