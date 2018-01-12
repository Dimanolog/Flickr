package com.github.dimanolog.flickr.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Dimanolog on 12.01.2018.
 */

public class CommentsActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context pContext) {
        Intent intent = new Intent();

        return new Intent(pContext, CommentsActivity.class);
    }
    @Override
    protected Fragment createFragment() {
        return null;
    }
}
