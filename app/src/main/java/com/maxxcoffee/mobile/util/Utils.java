package com.maxxcoffee.mobile.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.maxxcoffee.mobile.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Rio Swarawan on 3/14/2016.
 */
public class Utils {

    public static final int MORNING = 1001;
    public static final int AFTERNOON = 1002;
    public static final int EVENING = 1003;

    private static NetworkInfo networkInfo;

    public static Integer getAge(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthday);
        int thisYear = calendar.get(Calendar.YEAR);
        int birthYear = birthCalendar.get(Calendar.YEAR);

        return thisYear - birthYear;
    }

    public static String readFileFromAssets(Context context, String filename) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename, AssetManager.ACCESS_BUFFER), "UTF-8"));

            String mLine = reader.readLine();
            while (mLine != null) {
                sb.append(mLine);
                mLine = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static boolean isConnected(Context context) {
        return isConnectionOn(context);
    }

    private static boolean isConnectionOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }

    public static String convertDayToIndonesian(String day) {
        String result = "";
        if (day.equalsIgnoreCase("Sun")) {
            result = "Minggu";
        } else if (day.equalsIgnoreCase("Mon")) {
            result = "Senin";
        } else if (day.equalsIgnoreCase("Tue")) {
            result = "Selasa";
        } else if (day.equalsIgnoreCase("Wed")) {
            result = "Rabu";
        } else if (day.equalsIgnoreCase("Thu")) {
            result = "Kamis";
        } else if (day.equalsIgnoreCase("Fri")) {
            result = "Jumat";
        } else if (day.equalsIgnoreCase("Sat")) {
            result = "Sabtu";
        }
        return result;
    }

    public static int getDayPart() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return MORNING;
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            return AFTERNOON;
        } else if (timeOfDay >= 17 && timeOfDay <= 23) {
            return EVENING;
        }
        return -999;
    }

    public static String getDuration(Date createdDate) {
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_POST);
        Date today = new Date();
        long diff = today.getTime() - createdDate.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000) % 30;
        long diffMonth = diff / (30 * 24 * 60 * 60 * 1000);

        String durationText;

        if (diffMonth > 0) {
            durationText = df.format(createdDate);
        } else if (diffDays > 0) {
            durationText = diffDays + " hari yang lalu";
        } else if (diffHours > 0) {
            durationText = diffHours + " jam yang lalu";
        } else if (diffMinutes > 0) {
            durationText = diffMinutes + " menit yang lalu";
        } else {
            durationText = "baru saja";
        }
        return durationText;
    }

    public static long getDurationInMinutes(Date createdDate) {
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
        Date today = new Date();
        long diff = today.getTime() - createdDate.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        return diffMinutes;
    }

    public static long getDurationInDays(Date createdDate) {
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_META);
        Date today = new Date();
        long diff = today.getTime() - createdDate.getTime();

        long diffDays = diff / (1000 * 60 * 60 * 24);
        return diffDays;
    }

    public static Properties getProperties(Context context) {
        Properties properties = new Properties();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("app.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, float scale) {
        int width = bm.getWidth();
        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scale, scale);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    public static boolean isAllowed(){

        /*SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_POST);
        Date today = new Date();
        Date check = null;
        try {
            check = df.parse("17/12/2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (today.after(check)) return false;*/
        return true;
    }

    public static String chkStatus(Context context, String status) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (!status.equals("")){
            return status;
        } else {
            if (wifi.isConnectedOrConnecting ()) {
                return context.getResources().getString(R.string.data);
            } else if (mobile.isConnectedOrConnecting ()) {
                return context.getResources().getString(R.string.wifi);
            } else {
                return context.getResources().getString(R.string.something_wrong);
            }
        }
    }

}
