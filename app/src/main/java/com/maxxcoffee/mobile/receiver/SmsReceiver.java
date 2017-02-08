package com.maxxcoffee.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.maxxcoffee.mobile.ui.activity.VerificationActivity;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;

/**
 * Created by Ravi on 09/07/15.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("sms", "received");
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (senderAddress.toLowerCase().equalsIgnoreCase(Config.SMS_ORIGIN_1) ||
                            senderAddress.toLowerCase().equalsIgnoreCase(Config.SMS_ORIGIN_2)) {
                        // verification code from sms
                        String verificationCode = getVerificationCode(message);
                        Log.e(TAG, "OTP received: "+ String.valueOf(message.length()) + ", msg: " + verificationCode);
                        //set ke preference manager ya geng
                        PreferenceManager.putString(context, Constant.PREFERENCE_VERIFICATION_CODE, verificationCode);
                        VerificationActivity.verificationCode.setText(verificationCode);
                    }else{
                        Log.e(TAG, "SMS is not for our app!");
                    }

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        //String msg = message;
        String code = message.substring(message.length() - 5, message.length()-1);
        return code;
    }
}
