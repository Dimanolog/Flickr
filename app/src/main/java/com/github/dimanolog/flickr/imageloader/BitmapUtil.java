package com.github.dimanolog.flickr.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.dimanolog.flickr.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Dimanolog on 06.01.2018.
 */

public class BitmapUtil {

    private static final String TAG = BitmapUtil.class.getSimpleName();

    private BitmapUtil() {
    }
    //TODO is safe use stream  reset?
    public static Bitmap getScaledBitmap(InputStream pInputStream, int pWidth, int pHeight) throws IOException {

        InputStream bufferdInputStream = new BufferedInputStream(pInputStream, 8 * 1024 * 1024);
        bufferdInputStream.mark(2);
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bufferdInputStream, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options, pWidth, pHeight);
        bufferdInputStream.reset();
        return BitmapFactory.decodeStream(bufferdInputStream, null, options);

    }

    public static Bitmap getBitmap(InputStream pInputStream) throws IOException {
        return BitmapFactory.decodeStream(pInputStream);
    }

    private static int calculateSampleSize(BitmapFactory.Options pOptions, int pReqWidth, int pReqHeight) {

        final int height = pOptions.outHeight;
        final int width = pOptions.outWidth;
        int inSampleSize = 1;

        if (height > pReqHeight || width > pReqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= pReqHeight
                    && (halfWidth / inSampleSize) >= pReqWidth) {
                inSampleSize *= 2;
                halfHeight /= 2;
                halfWidth /= 2;
            }
        }
        LogUtil.d(TAG, "calculateSampleSize: " + inSampleSize);
        return inSampleSize;
    }
}
