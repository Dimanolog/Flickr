package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient;
import com.github.dimanolog.flickr.api.Response;
import com.github.dimanolog.flickr.api.interfaces.IResponse;

import java.util.HashMap;
import java.util.Map;

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

    public void setAutheficationMangerCallback(IManagerCallback<Uri> pIManagerCallback) {
        mIManagerCallback = pIManagerCallback;
    }

    private AutheficationManager(@NonNull Context pContext) {
        mContext = pContext.getApplicationContext();
        mUserSession = new UserSession();
    }

    public void onFlickrCallback(Uri pUri) {
        String oAuthVerifier = pUri.getQueryParameter("oauth_verifier");
        mUserSession.setOAuthVerifier(oAuthVerifier);
        getAccesToken(mUserSession);

    }

    private void getAccesToken(final UserSession pUserSession) {
        IRequest request = new IRequest() {

            private Response<Uri> mUriResponse;

            @Override
            public void onPreRequest() {

            }

            @Override
            public void runRequest() {
                IResponse<String> requestAccessToken = FlickrApiAuthorizationClient.getAccesToken(pUserSession);
                if (!requestAccessToken.isError()) {
                    Map<String, String> paramToValueMap = parseParametes(requestAccessToken.getResult());
                    mUserSession.setFullName(paramToValueMap.get("fullname"));
                    mUserSession.setUsernsid(paramToValueMap.get("user_nsid"));
                    mUserSession.setUserName(paramToValueMap.get("username"));
                    mUserSession.setOAuthToken(paramToValueMap.get("oauth_token"));
                }
            }

            @Override
            public void onPostRequest() {

            }
        };
        startLoading(request);
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
                    Map<String, String> paramValuePairs = parseParametes(requestToken.getResult());
                    String oAuthToken = paramValuePairs.get("oauth_token");
                    String oAuthTokenSecret = paramValuePairs.get("oauth_token_secret");

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
                } else {
                    mIManagerCallback.onError(mUriResponse.getError());
                }
            }
        };
        startLoading(request);
    }

    private Map<String, String> parseParametes(String pParameters) {
        Map<String, String> paramNameToValueMap = new HashMap<>();
        String[] arr = pParameters.split("&");
        for (String s : arr) {
            String[] a = s.split("=");
            paramNameToValueMap.put(a[0], Uri.decode(a[1]));
        }
        return paramNameToValueMap;
    }

    private void startLoading(@NonNull IRequest pRequest) {
        RequestTask requestTask = new RequestTask(pRequest);
        requestTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null);
    }

}
