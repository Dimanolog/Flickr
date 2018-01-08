package com.github.dimanolog.flickr.util;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Dimanolog on 07.01.2018.
 */

public class SecureUtil {
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String ENC = "UTF-8";

    private SecureUtil() {
    }

    public static String getMD5hashFromString(final String pString) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(pString.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByHmacSHA1(String pBase, String pKey) {
        try {
            byte[] keyBytes = pKey.getBytes(ENC);
            SecretKey secretKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(secretKey);
            byte[] encode = Base64.encode(mac.doFinal(pBase.getBytes(ENC)), Base64.DEFAULT);
            String s = new String(encode, ENC).trim();
            return s;
        } catch (Exception pE) {
            throw new RuntimeException("cant encrypt string");
        }
    }
}
