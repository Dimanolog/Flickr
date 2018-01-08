package com.github.dimanolog.flickr.dataloader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient;

/**
 * Created by Dimanolog on 08.01.2018.
 */

public class AutheficationManager {
    private static AutheficationManager sInstance;
    private IDataProviderCallback<Uri> mIDataProviderCallback;

    private Context mContext;

    public static AutheficationManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (PhotoDataManager.class) {
                if (sInstance == null) {
                    sInstance = new AutheficationManager(context);
                }
            }
        }
        return sInstance;
    }

    public void setIDataProviderCallback(IDataProviderCallback<Uri> pIDataProviderCallback) {
        mIDataProviderCallback = pIDataProviderCallback;
    }

    private AutheficationManager(@NonNull Context pContext) {
        mContext = pContext.getApplicationContext();
    }

    public  getAuthorizationUri(){
        IRequest request = new IRequest() {
            @Override
            public void onPreRequest() {
                mIDataProviderCallback.onStartLoading();
            }

            @Override
            public void runRequest() {
                FlickrApiAuthorizationClient.getUserAuthorizationUri();
            }

            @Override
            public void onPostRequest() {
                mIDataProviderCallback.onSuccessResult();
            }
        }
    }

}
