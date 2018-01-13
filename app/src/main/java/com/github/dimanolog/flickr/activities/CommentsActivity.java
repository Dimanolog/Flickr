package com.github.dimanolog.flickr.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.dimanolog.flickr.fragments.CommentsFragment;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

/**
 * Created by Dimanolog on 12.01.2018.
 */

public class CommentsActivity extends SingleFragmentActivity {
    private static final String EXTRA_PHOTO = "com.github.dimanolog.flickr.photo";

    public static Intent newIntent(Context pContext, IPhoto pPhoto) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PHOTO, pPhoto);

        return new Intent(pContext, CommentsActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        IPhoto photo = (IPhoto) extras.getSerializable(EXTRA_PHOTO);

        return CommentsFragment.newInstance(photo);
    }
}
