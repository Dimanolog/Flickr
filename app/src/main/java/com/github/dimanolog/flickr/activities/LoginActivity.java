package com.github.dimanolog.flickr.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.dimanolog.flickr.datamanagers.AutheficationManager;
import com.github.dimanolog.flickr.fragments.AuthorizationWebView;

/**
 * Created by Dimanolog on 07.01.2018.
 */

public class LoginActivity extends WebViewActivity {
    private static final String AUTH_BASE_URL = "https://www.flickr.com/services/oauth";

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, WebViewActivity.class);

        return i;
    }

    @Override
    protected Fragment createFragment() {
        return AuthorizationWebView.newInstance(null);
    }
}

