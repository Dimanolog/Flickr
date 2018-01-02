package com.github.dimanolog.flickr.imageloader;

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
import android.widget.ImageView;

import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.util.IOUtils;
import com.github.dimanolog.flickr.util.LogUtil;

import java.io.File;
import java.io.IOException;

public class VanGogh extends HandlerThread {
    private static final String TAG = VanGogh.class.getSimpleName();
    private static final int MESSAGE_DOWNLOAD = 0;

    private static volatile VanGogh sInstance = null;

    private Context mContext;
    private Handler mRequestHandler;
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;
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
        onInitialize();
    }

    private void onInitialize() {
        super.start();
        super.getLooper();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        int availMemorInBytes = 16 * 1024 * 1024;
        if (activityManager != null) {
            availMemorInBytes = activityManager.getMemoryClass() * 1024 * 1024;
        }
        mLruCache = new LruCache<>(availMemorInBytes / 8);
        mDiskLruCache = new DiskLruCache(mContext);

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
                    handleResponse(target);
                }
            }
        };
    }

    void queueThumbnail(ImageRequest target) {
        Uri uri = target.getUri();
        if (uri == null) {
            setPlaceHolder(target);
            LogUtil.d(TAG, "uri is null only set placeholder");
            return;
        }
        LogUtil.d(TAG, "Got a URL: " + uri.toString());
        Bitmap bitmap = mLruCache.get(uri.toString());
        if (bitmap != null) {
            setBitmap(target, bitmap);
        } else {
            setPlaceHolder(target);
            setTag(target);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }


    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleResponse(final ImageRequest target) {
        String url = target.getUri().toString();
        Bitmap bitmap;
        File file = mDiskLruCache.get(url);
        if (file != null) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            handleResponse(bitmap, target);
            return;
        }
        if (target.getTargetImageView().get() != null) {
            try {
                final byte[] bitmapBytes = IOUtils.toByteArray(new HttpClient().request(url));
                bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                LogUtil.d(TAG, "Bitmap created");
                mLruCache.put(url, bitmap);
                mDiskLruCache.add(url, bitmap);
                handleResponse(bitmap, target);

            } catch (IOException pE) {
                VanGoghCallback callback = target.getCallback();
                if (callback != null) {
                    LogUtil.e(TAG, "Error downloading image", pE);
                    callback.onError(pE);
                }
            }
        }
    }

    private void setTag(ImageRequest pTarget) {
        ImageView imageView = pTarget.getTargetImageView().get();
        if (imageView != null) {
            String url = pTarget.getUri().toString();
            imageView.setTag(url);
        }
    }

    private void handleResponse(final Bitmap pBitmap, final ImageRequest target) {
        if (pBitmap != null) {
            mResponseHandler.post(new Runnable() {
                public void run() {
                    setBitmap(target, pBitmap);
                }
            });
        }
    }

    private void setBitmap(ImageRequest target, Bitmap pBitmap) {
        ImageView imageView = target.getTargetImageView().get();
        if (imageView != null) {
            Object tag = imageView.getTag();
            if (tag != null && tag.equals(target.getUri().toString())) {
                imageView.setImageBitmap(pBitmap);
            }
        }
        if (target.getCallback() != null) {
            target.getCallback().onSuccess(pBitmap);
        }
    }

    private void setPlaceHolder(final ImageRequest target) {
        if (target.isPlaceHolderFlag()) {
            Drawable img = ResourcesCompat.getDrawable(mContext.getResources(),
                    target.getPlaceholderResId(), null);
            ImageView targetImageView = target.getTargetImageView().get();
            if (targetImageView != null) {
                targetImageView.setImageDrawable(img);
            }
        }
    }
}




