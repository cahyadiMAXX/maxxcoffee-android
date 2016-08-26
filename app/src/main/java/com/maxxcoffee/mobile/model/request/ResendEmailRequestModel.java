package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/25/16.
 */
public class ResendEmailRequestModel extends DefaultRequestModel{
    private String email;
    private String resend_email;
    private String resend_sms;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResend_email() {
        return resend_email;
    }

    public void setResend_email(String resend_email) {
        this.resend_email = resend_email;
    }

    public String getResend_sms() {
        return resend_sms;
    }

    public void setResend_sms(String resend_sms) {
        this.resend_sms = resend_sms;
    }
}
