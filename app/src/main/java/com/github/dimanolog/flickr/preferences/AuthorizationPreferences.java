package com.github.dimanolog.flickr.preferences;

import android.content.Context;
import android.preference.PreferenceManager;

import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.google.gson.Gson;

/**
 * Created by Dimanolog on 14.01.2018.
 */

public class AuthorizationPreferences {

    private static final String PREF_USER_SESSION = "user_session";

    public static UserSession getStoredUserSession(Context pContext, UserSession pUserSession) {
        String sessionJson = PreferenceManager.getDefaultSharedPreferences(pContext)
                .getString(PREF_USER_SESSION, null);
        if (sessionJson != null) {
            return new Gson().fromJson(sessionJson, UserSession.class);
        } else {
            return null;
        }
    }

    public static void setStoredQuery(Context pContext, UserSession pUserSession) {
        String json = new Gson().toJson(pUserSession);
        PreferenceManager.getDefaultSharedPreferences(pContext)
                .edit()
                .putString(PREF_USER_SESSION, json)
                .apply();
    }
}
