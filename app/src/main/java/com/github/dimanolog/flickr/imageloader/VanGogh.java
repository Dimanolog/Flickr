package com.github.dimanolog.flickr.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.util.IOUtils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VanGogh extends HandlerThread {
    private static final String TAG = VanGogh.class.getSimpleName();
    private static final int MESSAGE_DOWNLOAD = 0;

    static volatile VanGogh sInstance = null;

    private Context mContext;
    private Handler mRequestHandler;
    private ConcurrentMap<ImageRequest, String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler = new Handler(Looper.getMainLooper());


    public VanGogh with(Context pContext) {
        if (sInstance == null) {
            synchronized (VanGogh.class) {
                sInstance = new VanGogh(pContext);
                return sInstance;
            }
        }
        return sInstance;
    }

    VanGogh(Context pContext) {
        super(TAG);
        if (pContext == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        mContext = pContext.getApplicationContext();
    }

    public ImageRequest.ImageRequestBuilder load(String pUrl) {
        return new ImageRequest.ImageRequestBuilder(pUrl);
    }

    public ImageRequest.ImageRequestBuilder load(Uri pUri) {
        return new ImageRequest.ImageRequestBuilder(pUri);
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    ImageRequest target = (ImageRequest) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    public void queueThumbnail(ImageRequest target, String url) {
        Log.i(TAG, "Got a URL: " + url);

        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final ImageRequest target) {
        try {
            final String url = mRequestMap.get(target);

            if (url == null) {
                return;
            }

            final byte[] bitmapBytes = IOUtils.toByteArray(new HttpClient().request(url));

            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(new Runnable() {
                public void run() {
                    if (mRequestMap.get(target) != null) {
                        ImageView imageView = target.getTargetImageView();
                        if (imageView != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                        if(target.getCallback()!=null){
                            target.getCallback().onSuccess(bitmap);
                        }

                        return;
                    }
                    mRequestMap.remove(target);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
}




