

package com.github.dimanolog.flickr.util;


import android.util.Log;
import com.github.dimanolog.flickr.BuildConfig;

public class LogUtil {

    public static void message(int priority, String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.println(priority, tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable);
        }
    }

    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.println(Log.ERROR, tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.println(Log.DEBUG, tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.println(Log.VERBOSE, tag, message);
        }
    }
}
