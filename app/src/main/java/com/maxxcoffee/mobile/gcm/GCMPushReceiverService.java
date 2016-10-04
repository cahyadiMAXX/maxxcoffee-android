package com.maxxcoffee.mobile.gcm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.j256.ormlite.stmt.query.In;
import com.maxxcoffee.mobile.R;
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
                logoutNow();
            }else if(data.getString("message").equals("promo")){
                showNotif("ini promo", "tolong dilihat");
            }else if(data.getString("message").equals("event")){
                showLargeNotif("ini event", "tolong dilihat");
            }else if(data.getString("message").equals("link")){
                showLinkNotif("ini link", "tolong dilihat");
            }else if(data.getString("message").equals("linkgambar")){
                showLinkNotifWithImage("link dengan gambar", "tolong dilihat");
            }
        }

    }

    //This method is generating a notification and displaying the notification
    private void showNotif(String title, String message){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("content", MainActivity.PROMO);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_maxx);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        notificationManager.notify(1000, notification);
    }

    private void showLinkNotif(String title, String message){
        String url = "https://android.jlelse.eu/creating-an-intro-screen-for-your-app-using-viewpager-pagetransformer-9950517ea04f#.5vmrnxylg";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_maxx);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        notificationManager.notify(1000, notification);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showLinkNotifWithImage(String title, String message){
        String url = "https://android.jlelse.eu/creating-an-intro-screen-for-your-app-using-viewpager-pagetransformer-9950517ea04f#.5vmrnxylg";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_maxx);

        Bitmap bodyIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notif);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new Notification.BigPictureStyle().bigPicture(bodyIcon))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        notificationManager.notify(1000, notification);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showLargeNotif(String title, String message){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("content", MainActivity.EVENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_maxx);

        Bitmap bodyIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notif);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(message);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new Notification.BigPictureStyle().bigPicture(bodyIcon))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        notificationManager.notify(1000, notification);
    }

    private boolean checkUserAlreadySignIn() {
        return PreferenceManager.getBool(this, Constant.PREFERENCE_LOGGED_IN, false);
    }

    public void logoutNow() {
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, true);
        //PreferenceManager.clearPreference(GCMPushReceiverService.this);
        //DatabaseConfig db = new DatabaseConfig(GCMPushReceiverService.this);
        //db.clearAllTable();
        //Intent in = new Intent(getApplicationContext(), MainActivity.class);
        //in.putExtra("content", MainActivity.HOME);
        //in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(in);
    }
}
