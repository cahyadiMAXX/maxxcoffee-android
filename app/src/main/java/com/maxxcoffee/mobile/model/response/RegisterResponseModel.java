package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/7/16.
 */
public class RegisterResponseModel {

    /**
     * status : success or fail
     */
    private String status;
    private String messages;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String message) {
        this.messages = message;
    }
}
