package com.github.dimanolog.flickr.imageloader;


import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

class ImageRequest {
    private Uri mUri;
    private WeakReference<ImageView> mTargetImageView;
    private VanGoghCallback mCallback;
    private int mPlaceholderResId;
    private int mTargetHeight;
    private int mTargetWidth;
    private boolean mPlaceHolderFlag;

    ImageRequest(ImageRequestBuilder pBuilder) {
        mUri = pBuilder.mUri;
        mPlaceholderResId = pBuilder.mPlaceholderResID;
        mTargetImageView = new WeakReference<>(pBuilder.mTargetImageView);
        mTargetHeight = pBuilder.mTargetHeight;
        mTargetWidth = pBuilder.mTargetWidth;
        mCallback = pBuilder.mCallback;
        mPlaceHolderFlag=pBuilder.mPlaceHolderFlag;
    }

    Uri getUri() {
        return mUri;
    }

    WeakReference<ImageView> getTargetImageView() {
        return mTargetImageView;
    }

    VanGoghCallback getCallback() {
        return mCallback;
    }

    int getPlaceholderResId() {
        return mPlaceholderResId;
    }

    int getTargetHeight() {
        return mTargetHeight;
    }

    int getTargetWidth() {
        return mTargetWidth;
    }

    public boolean isPlaceHolderFlag() {
        return mPlaceHolderFlag;
    }

    static public final class ImageRequestBuilder {
        private final VanGogh mVanGogh;
        private Uri mUri;
        private ImageView mTargetImageView;
        private int mTargetHeight;
        private int mTargetWidth;
        private int mPlaceholderResID;
        private boolean mPlaceHolderFlag;
        private VanGoghCallback mCallback;

        ImageRequestBuilder(Uri pUri, VanGogh pVanGogh) {
            mUri = pUri;
            mVanGogh = pVanGogh;
        }

        ImageRequestBuilder(String pUrl, VanGogh pVanGogh) {
            if (!TextUtils.isEmpty(pUrl)) {
                mUri = Uri.parse(pUrl);
            } else {
                mUri = null;
            }
            mVanGogh = pVanGogh;
        }

        public ImageRequestBuilder resize(int pHeight, int pWidth) {
            mTargetHeight = pHeight;
            mTargetWidth = pWidth;

            return this;
        }

        public ImageRequestBuilder placeHolder(@DrawableRes int pResID) {
            mPlaceholderResID = pResID;
            mPlaceHolderFlag=true;
            return this;
        }

        public void into(ImageView pTarget) {
            mTargetImageView = pTarget;
            startImageRequest();
        }

        public void into(VanGoghCallback pCallback) {
            mCallback = pCallback;
            startImageRequest();
        }

        private void startImageRequest() {
            mVanGogh.queueThumbnail(new ImageRequest(this));
        }
    }
}
