package com.github.dimanolog.flickr.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Dimanolog on 15.10.2017.
 */

public class IOUtils {
    private static final String TAG = IOUtils.class.getSimpleName();

    private IOUtils() {
    }

    public static void close(Closeable pCloseable) {
        if (pCloseable != null) {
            try {
                pCloseable.close();
            } catch (IOException e) {
                LogUtil.e(TAG, "something's gone wrong", e);
            }
        }
    }

    public static byte[] toByteArray(InputStream pInputstream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] bytes = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(pInputstream.available());

            byte[] chunk = new byte[1 << 16];
            int bytesRead;
            while ((bytesRead = pInputstream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
            bytes = byteArrayOutputStream.toByteArray();
        } finally {
            close(pInputstream);
            close(byteArrayOutputStream);
        }
        return bytes;
    }

    public static String toString(InputStream pInputstream) throws IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            inputStreamReader = new InputStreamReader(pInputstream);
            reader = new BufferedReader(inputStreamReader);
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            close(pInputstream);
            close(inputStreamReader);
            close(reader);
        }
        return sb.toString();
    }
}
