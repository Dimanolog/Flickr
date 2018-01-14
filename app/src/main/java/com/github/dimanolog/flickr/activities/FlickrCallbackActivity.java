package com.github.dimanolog.flickr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.dimanolog.flickr.datamanagers.authorization.AutheficationManager;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;

/**
 * Created by dimanolog on 10.01.18.
 */

public class FlickrCallbackActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            AutheficationManager autheficationManager = AutheficationManager.getInstance(this);
            IManagerCallback<Void> iManagerCallback = new IManagerCallback<Void>() {
                @Override
                public void onStartLoading() {

                }

                @Override
                public void onSuccessResult(Void result) {
                    Intent intent = PhotoGalleryActivity.newIntent(FlickrCallbackActivity.this);
                    startActivity(intent);
                }

                @Override
                public void onError(Throwable t) {

                }
            };

            autheficationManager.onFlickrCallback(uri, iManagerCallback);
        }
    }
}
