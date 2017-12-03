package com.github.dimanolog.flickr.imageloader;

import android.graphics.Bitmap;

/**
 * Created by Dimanolog on 02.12.2017.
 */

public interface VanGoghCallback {
    public void onError();
    public void onSuccess(Bitmap pBitmap);
}
