package com.github.dimanolog.flickr.datamanagers.authorization;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient;
import com.github.dimanolog.flickr.api.Response;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.api.interfaces.IResponseStatus;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.IRequest;
import com.github.dimanolog.flickr.datamanagers.PhotoDataManager;
import com.github.dimanolog.flickr.preferences.AuthorizationPreferences;
import com.github.dimanolog.flickr.threading.RequestExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dimanolog on 08.01.2018.
 */

public class AuthorizationManager {
    private static AuthorizationManager sInstance;

    private UserSession mUserSession;
    private Context mContext;
    private FlickrApiAuthorizationClient mFlickrApiAuthorizationClient;

    public static AuthorizationManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (PhotoDataManager.class) {
                if (sInstance == null) {
                    sInstance = new AuthorizationManager(context);
                }
            }
        }
        return sInstance;
    }

    private AuthorizationManager(@NonNull Context pContext) {
        mContext = pContext.getApplicationContext();
        mUserSession = AuthorizationPreferences.getStoredUserSession(mContext);
        mFlickrApiAuthorizationClient = new FlickrApiAuthorizationClient();
    }

    public void onFlickrCallback(Uri pUri, IManagerCallback<Void> pResponseStatusCallback) {
        String oAuthVerifier = pUri.getQueryParameter("oauth_verifier");
        mUserSession.setOAuthVerifier(oAuthVerifier);
        getAccesToken(mUserSession, pResponseStatusCallback);

    }

    public void checkToken(final IManagerCallback<IResponseStatus> pResponseStatusCallback) {
        final IRequest request = new IRequest() {
            private IResponse<IResponseStatus> mStatusResponse;

            @Override
            public void onPreRequest() {
                pResponseStatusCallback.onStartLoading();
            }

            @Override
            public void runRequest() {
                mStatusResponse = mFlickrApiAuthorizationClient.checkToken(mUserSession.getOAuthToken());

            }

            @Override
            public void onPostRequest() {
                if (mStatusResponse.isError()) {
                    pResponseStatusCallback.onError(mStatusResponse.getError());
                } else {
                    pResponseStatusCallback.onSuccessResult(mStatusResponse.getResult());
                }
            }
        };
        RequestExecutor.executeRequestSerial(request);
    }


    public void getAccesToken(final UserSession pUserSession, final IManagerCallback<Void> pManagerCallback) {
        IRequest request = new IRequest() {
            private IResponse<String> mResponeAccessToken;

            @Override
            public void onPreRequest() {
                pManagerCallback.onStartLoading();
            }

            @Override
            public void runRequest() {
                mResponeAccessToken = mFlickrApiAuthorizationClient.getAccesToken(pUserSession);
                if (!mResponeAccessToken.isError()) {
                    Map<String, String> paramToValueMap = parseParametes(mResponeAccessToken.getResult());
                    mUserSession.setFullName(paramToValueMap.get("fullname"));
                    mUserSession.setUsernsid(paramToValueMap.get("user_nsid"));
                    mUserSession.setUserName(paramToValueMap.get("username"));
                    mUserSession.setOAuthToken(paramToValueMap.get("oauth_token"));
                    mUserSession.setOAuthTokenSecret(paramToValueMap.get("oauth_token_secret"));
                    AuthorizationPreferences.setUserSession(mContext, mUserSession);
                }
            }

            @Override
            public void onPostRequest() {
                if (!mResponeAccessToken.isError()) {
                    pManagerCallback.onSuccessResult(null);
                } else {
                    pManagerCallback.onError(mResponeAccessToken.getError());
                }
            }
        };
        RequestExecutor.executeRequestSerial(request);
    }


    public void getAuthorizationUri(final IManagerCallback<Uri> pManagerCallback) {
        IRequest request = new IRequest() {

            private Response<Uri> mUriResponse;

            @Override
            public void onPreRequest() {
                pManagerCallback.onStartLoading();
            }

            @Override
            public void runRequest() {
                IResponse<String> requestToken = mFlickrApiAuthorizationClient.getRequestToken();
                if (!requestToken.isError()) {
                    Map<String, String> paramValuePairs = parseParametes(requestToken.getResult());
                    String oAuthToken = paramValuePairs.get("oauth_token");
                    String oAuthTokenSecret = paramValuePairs.get("oauth_token_secret");
                    mUserSession = new UserSession();
                    mUserSession.setOAuthToken(oAuthToken);
                    mUserSession.setOAuthTokenSecret(oAuthTokenSecret);

                    Uri userAuthorizationUri = mFlickrApiAuthorizationClient.getUserAuthorizationUri(oAuthToken);

                    mUriResponse = new Response<>(userAuthorizationUri);

                } else {
                    mUriResponse = new Response<>(requestToken.getError());
                }
            }

            @Override
            public void onPostRequest() {
                if (!mUriResponse.isError()) {
                    pManagerCallback.onSuccessResult(mUriResponse.getResult());
                } else {
                    pManagerCallback.onError(mUriResponse.getError());
                }
            }
        };
        RequestExecutor.executeRequestSerial(request);
    }

    public UserSession getUserSession() {
        return mUserSession;
    }

    public boolean checkUserSession() {
        return mUserSession != null;
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
