package com.maxxcoffee.mobile.model.request;

/**
 * Created by rioswarawan on 7/17/16.
 */
public class VerifySmsCodeRequestModel extends DefaultRequestModel {

    private String email;
    private String verify_sms;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerify_sms() {
        return verify_sms;
    }

    public void setVerify_sms(String verify_sms) {
        this.verify_sms = verify_sms;
    }
}
