package com.github.dimanolog.flickr.datamanagers.authorization;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dimanolog on 09.01.2018.
 */

public class UserSession {
    @SerializedName("user_nsid")
    private String mUsernsid;
    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("oauth_token")
    private String mOAuthToken;
    @SerializedName("oauth_token_secret")
    private String mOAuthTokenSecret;
    @SerializedName("oauth_verifier")
    private String mOAuthVerifier;
    @SerializedName("user_name")
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
