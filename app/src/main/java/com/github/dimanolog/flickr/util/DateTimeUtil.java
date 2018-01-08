package com.github.dimanolog.flickr.util;

/**
 * Created by Dimanolog on 08.01.2018.
 */

public class DateTimeUtil {
    private DateTimeUtil() {

    }

    public static String  getCurrentTimeStampString() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

}
