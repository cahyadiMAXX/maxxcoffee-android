package com.maxxcoffee.mobile.model;

/**
 * Created by jemsnaban on 12/15/16.
 */

public class FeaturedItemModel {

    private String message;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FeaturedItemModel{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
