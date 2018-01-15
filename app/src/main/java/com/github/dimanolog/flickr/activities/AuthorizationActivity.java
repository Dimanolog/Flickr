package com.github.dimanolog.flickr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.authorization.AuthorizationManager;

public class AuthorizationActivity extends AppCompatActivity {

    private AuthorizationManager mAuthorizationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthorizationManager = AuthorizationManager.getInstance(this);
        if (mAuthorizationManager.checkUserSession()) {
            mAuthorizationManager.checkToken(new IManagerCallback<IResponseStatus>() {
                @Override
                public void onStartLoading() {

                }

                @Override
                public void onSuccessResult(IResponseStatus result) {
                    if (result.isSuccess()) {
                        startGalleryActivity();
                    } else {
                        getAccesToken();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    startGalleryActivity();
                }
            });
        } else {
            getAccesToken();
        }
    }

    private void startGalleryActivity() {
        Intent intent = PhotoGalleryActivity.newIntent(this);
        startActivity(intent);
    }

    private void getAccesToken() {
        mAuthorizationManager.getAuthorizationUri(new IManagerCallback<Uri>() {
            @Override
            public void onStartLoading() {

            }

            @Override
            public void onSuccessResult(Uri pResult) {
                Intent intent = WebViewActivity.newIntent(AuthorizationActivity.this, pResult);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
