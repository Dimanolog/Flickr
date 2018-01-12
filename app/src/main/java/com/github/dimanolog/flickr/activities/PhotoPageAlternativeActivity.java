package com.github.dimanolog.flickr.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.dimanolog.flickr.fragments.PhotoPageFragment;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

/**
 * Created by Dimanolog on 05.01.2018.
 */

public class PhotoPageAlternativeActivity extends SingleFragmentActivity {

    private static final String EXTRA_PHOTO ="com.github.dimanolog.flickr.photo";

    public static Intent newIntent(Context context, IPhoto pPhoto) {
        Intent i = new Intent(context, PhotoPageAlternativeActivity.class);
        i.putExtra(EXTRA_PHOTO, pPhoto);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        IPhoto photo = (IPhoto) extras.getSerializable(EXTRA_PHOTO);
        return PhotoPageFragment.newInstance(photo);
    }
}
