package com.github.dimanolog.flickr.imageloader;


import android.net.Uri;
import android.widget.ImageView;

class ImageRequest {
    private Uri mUri;
    private ImageView mTargetImageView;
    private VanGoghCallback mCallback;
    private int mPlaceholderResId;
    private int mTargetHeight;
    private int mTargetWidth;


    ImageRequest(ImageRequestBuilder pBuilder) {
        mUri = pBuilder.mUri;
        mPlaceholderResId = pBuilder.mPlaceholderResID;
        mTargetImageView = pBuilder.mTargetImageView;
        mTargetHeight = pBuilder.mTargetHeight;
        mTargetWidth = pBuilder.mTargetWidth;
        mCallback = pBuilder.mCallback;
    }

    public Uri getUri() {
        return mUri;
    }

    public ImageView getTargetImageView() {
        return mTargetImageView;
    }

    public VanGoghCallback getCallback() {
        return mCallback;
    }

    public int getPlaceholderResId() {
        return mPlaceholderResId;
    }

    public int getTargetHeight() {
        return mTargetHeight;
    }

    public int getTargetWidth() {
        return mTargetWidth;
    }

    static public final class ImageRequestBuilder {
        private final VanGogh mVanGogh;
        private Uri mUri;
        private ImageView mTargetImageView;
        private int mTargetHeight;
        private int mTargetWidth;
        private int mPlaceholderResID;
        private VanGoghCallback mCallback;

        ImageRequestBuilder(Uri pUri, VanGogh pVanGogh) {
            mUri = pUri;
            mVanGogh = pVanGogh;
        }

        ImageRequestBuilder(String pUrl, VanGogh pVanGogh) {
            mUri = Uri.parse(pUrl);
            mVanGogh = pVanGogh;
        }

        public ImageRequestBuilder resize(int pHeight, int pWidth) {
            mTargetHeight = pHeight;
            mTargetWidth = pWidth;

            return this;
        }

        public ImageRequestBuilder placeHolder(int pResID) {
            mPlaceholderResID = pResID;
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
