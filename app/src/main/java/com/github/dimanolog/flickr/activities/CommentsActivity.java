package com.github.dimanolog.flickr.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Dimanolog on 12.01.2018.
 */

public class CommentsActivity extends SingleFragmentActivity {
      private static final String EXTRA_PHOTO_ID ="com.github.dimanolog.flickr.photo_id";

    public static Intent newIntent(Context pContext, long pPhotoId) {
        Intent intent = new Intent();
        intent.putExtra( EXTRA_PHOTO_ID, pPhotoId );
        return new Intent(pContext, CommentsActivity.class);
    }
    @Override
    protected Fragment createFragment() {
        return null;
    }
}
