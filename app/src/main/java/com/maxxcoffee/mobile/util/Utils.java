package com.maxxcoffee.mobile.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rio Swarawan on 3/14/2016.
 */
public class Utils {

    public static final int MORNING = 1001;
    public static final int AFTERNOON = 1002;
    public static final int EVENING = 1003;
    public static final int NIGHT = 1004;

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

        if (timeOfDay >= 5 && timeOfDay < 12) {
            return MORNING;
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            return AFTERNOON;
        } else if (timeOfDay >= 17 && timeOfDay < 22) {
            return EVENING;
        } else if (timeOfDay >= 22 || (timeOfDay >= 0 && timeOfDay < 5)) {
            return NIGHT;
        }
        return -999;
    }

    public static String getDuration(Date createdDate) {
        SimpleDateFormat df = new SimpleDateFormat(Constant.DATEFORMAT_POST);
        Date today = new Date();
        long diff = today.getTime() - createdDate.getTime();
//        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000) % 30;
        long diffMonth = diff / (30 * 24 * 60 * 60 * 1000);

        String durationText;

        if (diffMonth > 0) {
//            durationText = diffMonth + " MONTH AGO";
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

}
