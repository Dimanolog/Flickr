package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.media.MediaCas;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient;
import com.github.dimanolog.flickr.api.Response;
import com.github.dimanolog.flickr.api.interfaces.IResponse;

/**
 * Created by Dimanolog on 08.01.2018.
 */

public class AutheficationManager {
    private static AutheficationManager sInstance;
    private IManagerCallback<Uri> mIManagerCallback;
    private UserSession mUserSession;
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

    public void setIDataProviderCallback(IManagerCallback<Uri> pIManagerCallback) {
        mIManagerCallback = pIManagerCallback;
    }

    private AutheficationManager(@NonNull Context pContext) {
        mContext = pContext.getApplicationContext();
    }

    public void getAuthorizationUri() {
        IRequest request = new IRequest() {

            private Response<Uri> mUriResponse;

            @Override
            public void onPreRequest() {
                mIManagerCallback.onStartLoading();
            }

            @Override
            public void runRequest() {
                IResponse<String> requestToken = FlickrApiAuthorizationClient.getRequestToken();
                if (!requestToken.isError()) {
                    Uri parse = Uri.parse(requestToken.getResult());
                    String oAuthToken = parse.getQueryParameter("oauth_token");
                    String oAuthTokenSecret = parse.getQueryParameter("oauth_token_secret");

                    mUserSession.setOAuthToken(oAuthToken);
                    mUserSession.setOAuthTokenSecret(oAuthTokenSecret);

                    Uri userAuthorizationUri = FlickrApiAuthorizationClient.getUserAuthorizationUri(oAuthToken);

                    mUriResponse = new Response<>(userAuthorizationUri);

                } else {
                    mUriResponse = new Response<>(requestToken.getError());
                }

            }

            @Override
            public void onPostRequest() {
                if (!mUriResponse.isError()) {
                    mIManagerCallback.onSuccessResult(mUriResponse.getResult());
                }else {
                    mIManagerCallback.onError(mUriResponse.getError());
                }
            }
        };
    }

    private void startLoading(@NonNull IRequest pRequest) {
        RequestTask requestTask = new RequestTask(pRequest);
        requestTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null);
    }

}
