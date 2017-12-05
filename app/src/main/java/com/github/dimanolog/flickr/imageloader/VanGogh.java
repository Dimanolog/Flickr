package com.github.dimanolog.flickr.imageloader;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.github.dimanolog.flickr.R;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.util.IOUtils;

import java.io.IOException;

public class VanGogh extends HandlerThread {
    private static final String TAG = VanGogh.class.getSimpleName();
    private static final int MESSAGE_DOWNLOAD = 0;

    private static volatile VanGogh sInstance = null;

    private Context mContext;
    private Handler mRequestHandler;
    private LruCache<String, Bitmap> mLruCache;

    //private ConcurrentMap<ImageRequest, String> mRequestMap = new ConcurrentHashMap<>();
    private Handler mResponseHandler = new Handler(Looper.getMainLooper());


    public static VanGogh with(Context pContext) {
        if (sInstance == null) {
            synchronized (VanGogh.class) {
                sInstance = new VanGogh(pContext);
                return sInstance;
            }
        }
        return sInstance;
    }

    private VanGogh(Context pContext) {
        super(TAG);
        if (pContext == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        mContext = pContext.getApplicationContext();
        onInitilize();
    }

    private void onInitilize() {
        super.start();
        super.getLooper();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        int availMemorInBytes = activityManager.getMemoryClass() * 1024 * 1024;
        mLruCache = new LruCache<String, Bitmap>(availMemorInBytes/8);

    }


    public ImageRequest.ImageRequestBuilder load(String pUrl) {
        return new ImageRequest.ImageRequestBuilder(pUrl, this);
    }

    public ImageRequest.ImageRequestBuilder load(Uri pUri) {
        return new ImageRequest.ImageRequestBuilder(pUri, this);
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    ImageRequest target = (ImageRequest) msg.obj;
                    handleRequest(target);
                }
            }
        };
    }

    void queueThumbnail(ImageRequest target) {
        Log.i(TAG, "Got a URL: " + target.getUri().toString());
        mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                .sendToTarget();
        beforeLoading(target);
    }


    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final ImageRequest target) {
        String url = target.getUri().toString();
        Bitmap bitmap=mLruCache.get(url);
        if(bitmap!=null){
            handleRequest(bitmap,target);
            return;
        }

        if(target.getTargetImageView()!=null) {
            try {
                final byte[] bitmapBytes = IOUtils.toByteArray(new HttpClient().request(url));
                bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                Log.i(TAG, "Bitmap created");
                mLruCache.put(url, bitmap);
                handleRequest(bitmap, target);

            } catch (IOException ioe) {
                Log.e(TAG, "Error downloading image", ioe);
            }
        }
    }


    private void handleRequest(final Bitmap pBitmap, final ImageRequest target){
        mResponseHandler.post(new Runnable() {
            public void run() {
                ImageView imageView = target.getTargetImageView();
                if (imageView != null) {
                    imageView.setImageBitmap(pBitmap);
                }
                if (target.getCallback() != null) {
                    target.getCallback().onSuccess(pBitmap);
                }
            }
        });
    }

    private void beforeLoading(final ImageRequest target) {
        Drawable img = ResourcesCompat.getDrawable(mContext.getResources(),
                target.getPlaceholderResId(), null);
        if (target.getTargetImageView() != null) {
            target.getTargetImageView().setImageDrawable(img);
        }
    }
}




