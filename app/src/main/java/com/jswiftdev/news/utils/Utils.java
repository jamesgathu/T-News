package com.jswiftdev.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

/**
 * contains reusable methods from any part of the application
 */
public class Utils {
    /**
     * network is available and active connection is WIFI
     */
    private static int TYPE_WIFI = 1;

    /**
     * network is available and active connection is Mobile Data
     */
    private static int TYPE_MOBILE = 2;

    /**
     * network is not available
     */
    private static int TYPE_NOT_CONNECTED = 0;


    /**
     * checks if an email address if valid
     *
     * @param emailAddress to be validated
     * @return true if string is a valid email address
     */
    public static boolean isEmailAddressValid(String emailAddress) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailAddress.matches(emailPattern);
    }


    /**
     * takes in a string representation of time similar to {@link Constants#SERVER_DATE_FORMAT}
     * and finds its relative time that is more human readable
     *
     * @param dateToFind unformatted  datetime string
     * @return relative time String <i>e.g 5 hours ago, yesterday e.t.c</i>
     */
    public static String getRelativeTime(String dateToFind) {
        DateTime dateNow = DateTime.now();
        DateTime dateToCompare = (DateTimeFormat.forPattern(Constants.SERVER_DATE_FORMAT)).parseDateTime(dateToFind);

        int seconds, minutes, hours, days;

        Duration duration = new Duration(dateToCompare, dateNow);
        seconds = (int) duration.getStandardSeconds();
        minutes = (int) duration.getStandardMinutes();
        hours = (int) duration.getStandardHours();
        days = (int) duration.getStandardDays();

        if (seconds < 15)
            return "Just Now";

        if (seconds >= 15 && seconds < 60) {
            return seconds + " seconds ago";
        }

        if (minutes >= 0 && minutes < 60) {
            if (minutes == 1)
                return minutes + " minute ago";

            return minutes + " minutes ago";
        }


        if (minutes >= 60 && hours < 24)
            return hours + " hours ago";

        if (hours == 24 || days == 1)
            return "Yesterday";

        if (days > 1)
            return days + " days ago";

        if (days >= 6)
            return days / 7 + " weeks ago";

        if ((days / 7) > 4)
            return days / 30 + " months ago";

        Log.d(Constants.LOG_TAG, "Failed " + dateToFind + " hours -> " + hours + "\tdays -> " + days);
        return "Not applicable";
    }


    /**
     * get connectivity status as {@link #TYPE_MOBILE},  {@link #TYPE_WIFI} or {@link #TYPE_NOT_CONNECTED}
     *
     * @param context for the caller
     * @return constant representative of the status
     */
    static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = Utils.getConnectivityStatus(context);
        int status = 0;
        if (conn == Utils.TYPE_WIFI) {
            status = TYPE_WIFI;
        } else if (conn == Utils.TYPE_MOBILE) {
            status = TYPE_MOBILE;
        } else if (conn == Utils.TYPE_NOT_CONNECTED) {
            status = TYPE_NOT_CONNECTED;
        }
        return status;
    }


    /**
     * check for active connection to the internet either Data or Wifi
     *
     * @param context from which the caller belongs to
     * @return true if the is an active connection
     */
    public static boolean isConnected(Context context) {
        return getConnectivityStatus(context) != TYPE_NOT_CONNECTED;
    }
}
