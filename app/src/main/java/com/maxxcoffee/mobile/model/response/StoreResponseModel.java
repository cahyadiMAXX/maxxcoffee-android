package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class StoreResponseModel {
    private String status;
    private List<StoreItemResponseModel> result;
    private List<StoreItemResponseModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StoreItemResponseModel> getResult() {
        return result;
    }

    public void setResult(List<StoreItemResponseModel> result) {
        this.result = result;
    }

    public List<StoreItemResponseModel> getData() {
        return data;
    }

    public void setData(List<StoreItemResponseModel> data) {
        this.data = data;
    }
}
