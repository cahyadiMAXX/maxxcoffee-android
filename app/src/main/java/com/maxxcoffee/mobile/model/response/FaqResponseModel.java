package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/25/16.
 */
public class FaqResponseModel {

    private String status;
    private List<FaqItemResponseModel> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FaqItemResponseModel> getResult() {
        return result;
    }

    public void setResult(List<FaqItemResponseModel> result) {
        this.result = result;
    }
}
