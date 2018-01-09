package com.github.dimanolog.flickr.datamanagers;

/**
 * Created by Dimanolog on 09.01.2018.
 */

public class UserSession {
    private String mUsernsid;
    private String mFullName;
    private String mOAuthToken;
    private String moAuthTokenSecret;

    public String getUsernsid() {
        return mUsernsid;
    }

    public void setUsernsid(String pUsernsid) {
        mUsernsid = pUsernsid;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String pFullName) {
        mFullName = pFullName;
    }

    public String getOAuthToken() {
        return mOAuthToken;
    }

    public void setOAuthToken(String pOAuthToken) {
        mOAuthToken = pOAuthToken;
    }

    public String getMoAuthTokenSecret() {
        return moAuthTokenSecret;
    }

    public void setMoAuthTokenSecret(String pMoAuthTokenSecret) {
        moAuthTokenSecret = pMoAuthTokenSecret;
    }
}
