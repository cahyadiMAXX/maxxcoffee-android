package com.maxxcoffee.mobile.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.j256.ormlite.stmt.query.In;
import com.maxxcoffee.mobile.activity.MainActivity;
import com.maxxcoffee.mobile.database.DatabaseConfig;
import com.maxxcoffee.mobile.model.request.GCMRequestModel;
import com.maxxcoffee.mobile.task.LogoutAllMyDevicesTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {
    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        for (String key: data.keySet())
        {
            Log.d ("myApplication", key + " is a key in the bundle");
            Log.d ("myApplication_msg", data.getString(key) + " is a message of " +key+ " in the bundle");
        }

        //klo udah login
        if(checkUserAlreadySignIn()){
            if(data.getString("message").equals("logout")){
                //logoutNow();
            }
        }else{

        }

    }

    //This method is generating a notification and displaying the notification
    private void showNotif(String title, String message){
        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        intent = new Intent(String.valueOf(MainActivity.class));
        intent.putExtra("message", message);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_b);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);

     *//*   RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notif_layout);
        contentView.setImageViewResource(R.id.image, R.drawable.logo_b_notif);
        contentView.setTextViewText(R.id.title, "This is shit lah");
        contentView.setTextViewText(R.id.text, message);*//*

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_b)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        notificationManager.notify(1000, notification);*/
    }

    private boolean checkUserAlreadySignIn() {
        return PreferenceManager.getBool(this, Constant.PREFERENCE_LOGGED_IN, false);
    }

    public void logoutNow() {
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, true);
        PreferenceManager.clearPreference(this);
        DatabaseConfig db = new DatabaseConfig(this);
        db.clearAllTable();
        ///prepareDrawerList();
        //logoutAllMyDevices();
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.putExtra("content", MainActivity.HOME);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
    }

    public void logoutAllMyDevices(){
        GCMRequestModel body = new GCMRequestModel();
        body.setEmail(PreferenceManager.getString(getApplicationContext(), Constant.PREFERENCE_EMAIL, ""));

        LogoutAllMyDevicesTask task = new LogoutAllMyDevicesTask(getApplicationContext()) {
            @Override
            public void onSuccess(String message) {
                //PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, true);
                //Log.d("logoutallmydevices", message);
            }

            @Override
            public void onFailed() {
                Log.d("logoutallmydevices", "failed");
            }
        };
        task.execute(body);
    }
}
