package com.github.dimanolog.flickr.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.github.dimanolog.flickr.fragments.WebViewFragment;

public class PhotoPageActivity extends SingleFragmentActivity {

    protected WebViewFragment.OnBackPressedListener onBackPressedListener;

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent i = new Intent(context, PhotoPageActivity.class);
        i.setData(photoPageUri);
        return i;
    }
    public void setOnBackPressedListener(WebViewFragment.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
    @Override
    protected Fragment createFragment() {
        return WebViewFragment.newInstance(getIntent().getData());
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener.doBack()){
            super.onBackPressed();
        }
    }
}
