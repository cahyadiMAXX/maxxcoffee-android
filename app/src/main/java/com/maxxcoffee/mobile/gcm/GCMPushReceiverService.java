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
import com.maxxcoffee.mobile.task.DownloadImageTask;
import com.maxxcoffee.mobile.task.LogoutAllMyDevicesTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;


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
            //Log.d ("myApplication", key + " is a key in the bundle");
            Log.d ("myApplication_msg", data.getString(key) + " - " +key+ " in the bundle");
        }


        String[] listType = {"Link", "Home", "Menu", "Store", "Promo",
                "Event", "My Card", "Add Card",
                "Balance Transfer", "Card History", "Report Lost Card",
                "About", "FAQ", "TOS", "Contact Us", "Profile", "Logout"};

        if(checkUserAlreadySignIn()){
            if(data.getString("tipe").equalsIgnoreCase("logout")){ //logout
                logoutNow();
            }else if(data.getString("tipe").equalsIgnoreCase("link")){ //link
                showLinkNotifWithImage(data);
            }else if(data.getString("tipe").equalsIgnoreCase("home")){//home
                showLargeNotif(MainActivity.HOME, data);
            }else if(data.getString("tipe").equalsIgnoreCase("menu")){//menu
                showLargeNotif(MainActivity.MENU, data);
            }else if(data.getString("tipe").equalsIgnoreCase("store")){//store
                showLargeNotif(MainActivity.STORE, data);
            }else if(data.getString("tipe").equalsIgnoreCase("promo")){ //promo
                showLargeNotif(MainActivity.PROMO, data);
            }else if(data.getString("tipe").equalsIgnoreCase("event")){ //event
                showLargeNotif(MainActivity.EVENT, data);
            }else if(data.getString("tipe").equalsIgnoreCase("my card")){ //my card
                showLargeNotif(MainActivity.MY_CARD, data);
            }else if(data.getString("tipe").equalsIgnoreCase("add card")){ //add card
                showLargeNotif(MainActivity.MY_CARD, data);
            }else if(data.getString("tipe").equalsIgnoreCase("balance transfer")){ //balance transfer
                showLargeNotif(MainActivity.BALANCE_TRANSFER, data);
            }else if(data.getString("tipe").equalsIgnoreCase("card history")){ //card history
                showLargeNotif(MainActivity.CARD_HISTORY, data);
            }else if(data.getString("tipe").equalsIgnoreCase("report lost card")){ //report lost card
                showLargeNotif(MainActivity.REPORT_LOST_CARD, data);
            }else if(data.getString("tipe").equalsIgnoreCase("about")){ //about
                showLargeNotif(MainActivity.ABOUT, data);
            }else if(data.getString("tipe").equalsIgnoreCase("faq")){ //faq
                showLargeNotif(MainActivity.FAQ, data);
            }else if(data.getString("tipe").equalsIgnoreCase("tos")){ //tos
                showLargeNotif(MainActivity.TOS, data);
            }else if(data.getString("tipe").equalsIgnoreCase("contact us")){ //contact us
                showLargeNotif(MainActivity.CONTACT_US, data);
            }else if(data.getString("tipe").equalsIgnoreCase("profile")){ //profile
                showLargeNotif(MainActivity.PROFILE, data);
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
    private void showLinkNotifWithImage(final Bundle data){
        String url = "http://google.com";
        String image = "";

        boolean isImageExists = false;

        for (String key: data.keySet())
        {
            if(key.equals("image")){
                isImageExists = true;
                image = data.getString("image");
                break;
            }
        }

        try{
            url = data.getString("link");
        }catch (Exception e){
        }

        //String image = "https://android.jlelse.eu/creating-an-intro-screen-for-your-app-using-viewpager-pagetransformer-9950517ea04f#.5vmrnxylg";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        final NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        final Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_maxx);

        if(isImageExists){
            //Bitmap bodyIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notif);

            DownloadImageTask task = new DownloadImageTask(getApplicationContext()) {
                @Override
                protected void onDownloadError() {
                    //Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageView);
                    Log.d("errorglide", "glide error bos");
                }

                @Override
                protected void onImageDownloaded(Bitmap bitmap) {
                    Bitmap bodyIcon = Utils.getResizedBitmap(bitmap, 0.98f);

                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setBigContentTitle(data.getString("title"));
                    bigPictureStyle.setSummaryText(data.getString("subtitle"));

                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                            .setLargeIcon(largeIcon)
                            .setContentTitle(data.getString("title"))
                            .setContentText(data.getString("subtitle"))
                            .setAutoCancel(true)
                            .setStyle(new Notification.BigPictureStyle().bigPicture(bodyIcon))
                            .setContentIntent(pendingIntent)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .build();

                    notificationManager.notify(1000, notification);
                }
            };
            task.execute(image);
        }else{
            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(data.getString("title"))
                    .setContentText(data.getString("subtitle"))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(data.getString("subtitle")))
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build();

            notificationManager.notify(1000, notification);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showLargeNotif(int content, final Bundle data){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent.putExtra("message", message);
        intent.putExtra("content", content);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        final NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String url = "";
        boolean isImageExists = false;

        for (String key: data.keySet())
        {
            if(key.equals("image")){
                isImageExists = true;
                url = data.getString("image");
                break;
            }
        }

        final Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_maxx);

        if(isImageExists){
            //Bitmap bodyIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notif);

            DownloadImageTask task = new DownloadImageTask(getApplicationContext()) {
                @Override
                protected void onDownloadError() {
                    //Glide.with(activity).load("").placeholder(R.drawable.ic_no_image).into(imageView);
                    Log.d("errorglide", "glide error bos");
                }

                @Override
                protected void onImageDownloaded(Bitmap bitmap) {
                    Bitmap bodyIcon = Utils.getResizedBitmap(bitmap, 0.98f);

                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setBigContentTitle(data.getString("title"));
                    bigPictureStyle.setSummaryText(data.getString("subtitle"));

                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                            .setLargeIcon(largeIcon)
                            .setContentTitle(data.getString("title"))
                            .setContentText(data.getString("subtitle"))
                            .setAutoCancel(true)
                            .setStyle(new Notification.BigPictureStyle().bigPicture(bitmap))
                            .setContentIntent(pendingIntent)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .build();

                    notificationManager.notify(1000, notification);
                }
            };
            task.execute(url);
        }else{
            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? android.R.color.transparent : R.drawable.logo_maxx)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(data.getString("title"))
                    .setContentText(data.getString("subtitle"))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(data.getString("subtitle")))
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .build();

            notificationManager.notify(1000, notification);
        }
    }

    private boolean checkUserAlreadySignIn() {
        return PreferenceManager.getBool(this, Constant.PREFERENCE_LOGGED_IN, false);
    }

    public void logoutNow() {
        PreferenceManager.putBool(getApplicationContext(), Constant.PREFERENCE_LOGOUT_NOW, true);
        PreferenceManager.clearPreference(GCMPushReceiverService.this);
        DatabaseConfig db = new DatabaseConfig(GCMPushReceiverService.this);
        db.clearAllTable();
        PreferenceManager.putBool(this, Constant.PREFERENCE_TUTORIAL_SKIP, true);
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.putExtra("content", MainActivity.HOME);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }
}
