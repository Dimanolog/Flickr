package com.github.dimanolog.flickr.dataloader;

/**
 * Created by Dimanolog on 09.01.2018.
 */

public class UserSession {
    String usernsid;
    String fullName;
    String oAuthToken;
    String oAuthTokenSecret;

    public String getUsernsid() {
        return usernsid;
    }

    public void setUsernsid(String pUsernsid) {
        usernsid = pUsernsid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String pFullName) {
        fullName = pFullName;
    }

    public String getoAuthToken() {
        return oAuthToken;
    }

    public void setoAuthToken(String pOAuthToken) {
        oAuthToken = pOAuthToken;
    }

    public String getoAuthTokenSecret() {
        return oAuthTokenSecret;
    }

    public void setoAuthTokenSecret(String pOAuthTokenSecret) {
        oAuthTokenSecret = pOAuthTokenSecret;
    }
}
