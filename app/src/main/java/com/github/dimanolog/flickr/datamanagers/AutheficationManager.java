package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient;
import com.github.dimanolog.flickr.api.Response;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.threading.RequestExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dimanolog on 08.01.2018.
 */

public class AutheficationManager {
    private static AutheficationManager sInstance;
    private IManagerCallback<Uri> mManagerCallback;
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
        mManagerCallback = pIManagerCallback;
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
            private IResponse<String> mResponeAccessToken;

            @Override
            public void onPreRequest() {
                mManagerCallback.onStartLoading();
            }

            @Override
            public void runRequest() {
                mResponeAccessToken = FlickrApiAuthorizationClient.getAccesToken(pUserSession);
                if (!mResponeAccessToken.isError()) {
                    Map<String, String> paramToValueMap = parseParametes(mResponeAccessToken.getResult());
                    mUserSession.setFullName(paramToValueMap.get("fullname"));
                    mUserSession.setUsernsid(paramToValueMap.get("user_nsid"));
                    mUserSession.setUserName(paramToValueMap.get("username"));
                    mUserSession.setOAuthToken(paramToValueMap.get("oauth_token"));
                    mUserSession.setOAuthTokenSecret(paramToValueMap.get("oauth_token_secret"));
                }
            }

            @Override
            public void onPostRequest() {
                if (!mResponeAccessToken.isError()) {
                    mManagerCallback.onSuccessResult(null);
                } else {
                    mManagerCallback.onError(mResponeAccessToken.getError());
                }
            }
        };
        RequestExecutor.executeRequestSerial(request);
    }


    public void getAuthorizationUri() {
        IRequest request = new IRequest() {

            private Response<Uri> mUriResponse;

            @Override
            public void onPreRequest() {
                mManagerCallback.onStartLoading();
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
                    mManagerCallback.onSuccessResult(mUriResponse.getResult());
                } else {
                    mManagerCallback.onError(mUriResponse.getError());
                }
            }
        };
        RequestExecutor.executeRequestSerial(request);
    }

    public UserSession getUserSession() {
        return mUserSession;
    }
    private Map<String, String> parseParametes(String pParameters) {
        Map<String, String> paramNameToValueMap = new HashMap<>();
        String[] arr = pParameters.split("&");
        for (String s : arr) {
            String[] a = s.split("=");
            paramNameToValueMap.put(a[0], Uri.decode(a[1]).trim());
        }
        return paramNameToValueMap;
    }

}
