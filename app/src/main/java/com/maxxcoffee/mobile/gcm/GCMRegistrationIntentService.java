package com.maxxcoffee.mobile.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.maxxcoffee.mobile.model.request.GCMRequestModel;
import com.maxxcoffee.mobile.task.extra.UpdateDeviceTokenTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;


/**
 * Created by Belal on 4/15/2016.
 */
public class GCMRegistrationIntentService extends IntentService {
    //Constants for success and errors
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    //Class constructor
    public GCMRegistrationIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Registering gcm to the device
        registerGCM();
    }

    private void registerGCM() {
        //Registration complete intent initially null
        Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
        String token = null;
        try {
            //Creating an instanceid
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //Getting the token from the instance id
            token = instanceID.getToken(("783321546063"), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server
            Log.i("GCMRegIntentService", "token:" + token);

            //on registration complete creating intent with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //Putting the token to the intent
            registrationComplete.putExtra("token", token);
            updateDeviceToken(token);
        } catch (Exception e) {
            //If any error occurred
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    //update ke server sendiri bos
    private void updateDeviceToken(String token){
        PreferenceManager.putString(getApplicationContext(), Constant.PREFERENCE_DEVICE_TOKEN, token);
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        GCMRequestModel gcmRequestModel = new GCMRequestModel();
        gcmRequestModel.setDevice_token(token);
        gcmRequestModel.setDevice_id(deviceId);
        gcmRequestModel.setEmail(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_EMAIL, ""));
        gcmRequestModel.setTipe("android");

        final UpdateDeviceTokenTask task = new UpdateDeviceTokenTask(getApplicationContext()) {
            @Override
            public void onSuccess(String message) {
                //Toast.makeText(getApplicationContext(), message + " device token", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed() {
                //Toast.makeText(getApplicationContext(), "Failed token", Toast.LENGTH_LONG).show();
            }
        };
        task.execute(gcmRequestModel);
    }
}
