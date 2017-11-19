package com.github.dimanolog.flickr.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Dimanolog on 15.10.2017.
 */

public class IOUtils {

    private IOUtils(){}

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
               Log.e("IOUtils", e.getMessage());
            }
        }
    }




    public static String toString(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            close(inputStream);
            close(inputStreamReader);
            close(reader);
        }
        return sb.toString();
    }
}
