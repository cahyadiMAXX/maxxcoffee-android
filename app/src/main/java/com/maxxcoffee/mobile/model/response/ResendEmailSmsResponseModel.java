package com.maxxcoffee.mobile.model.response;

/**
 * Created by rioswarawan on 7/26/16.
 */
public class ResendEmailSmsResponseModel extends DefaultResponseModel{

    private String message;
    private String wait_second;

    public String getWait_second() {
        return wait_second;
    }

    public void setWait_second(String wait_second) {
        this.wait_second = wait_second;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
