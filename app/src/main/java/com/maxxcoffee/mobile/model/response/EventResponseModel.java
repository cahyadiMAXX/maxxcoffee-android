package com.maxxcoffee.mobile.model.response;

import java.util.List;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class EventResponseModel {
    private String status;
    private List<EventItemResponseModel> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EventItemResponseModel> getResult() {
        return result;
    }

    public void setResult(List<EventItemResponseModel> result) {
        this.result = result;
    }
}
