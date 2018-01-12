package com.github.dimanolog.flickr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.dimanolog.flickr.datamanagers.AutheficationManager;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;

/**
 * Created by dimanolog on 10.01.18.
 */

public class AuthorizationActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            AutheficationManager autheficationManager = AutheficationManager.getInstance(this);
            autheficationManager.setAutheficationMangerCallback(new IManagerCallback<Uri>() {
                @Override
                public void onStartLoading() {

                }

                @Override
                public void onSuccessResult(Uri result) {
                    Intent intent = PhotoGalleryActivity.newIntent(AuthorizationActivity.this);
                    startActivity(intent);
                }

                @Override
                public void onError(Throwable t) {

                }
            });
            autheficationManager.onFlickrCallback(uri);
        }

    }
}
