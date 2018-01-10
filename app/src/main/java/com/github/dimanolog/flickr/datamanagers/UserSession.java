package com.github.dimanolog.flickr.datamanagers;

/**
 * Created by Dimanolog on 09.01.2018.
 */

public class UserSession {
    private String mUsernsid;
    private String mFullName;
    private String mOAuthToken;
    private String mOAuthTokenSecret;
    private String mOAuthVerifier;
    private String mUserName;

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

    public String getOAuthTokenSecret() {
        return mOAuthTokenSecret;
    }

    public void setOAuthTokenSecret(String pOAuthTokenSecret) {
        mOAuthTokenSecret = pOAuthTokenSecret;
    }

    public String getOAuthVerifier() {
        return mOAuthVerifier;
    }

    public void setOAuthVerifier(String pOAuthVerifier) {
        mOAuthVerifier = pOAuthVerifier;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String pUserName) {
        mUserName = pUserName;
    }
}
