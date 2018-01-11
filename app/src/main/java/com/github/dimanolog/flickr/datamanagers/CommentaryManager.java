/*
 * Copyright (c) 2018 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;

/**
 * Created by dimanolog on 11.01.18.
 */

public class CommentaryManager {
    private static CommentaryManager sInstance;
    private Context mContext;
    private IManagerCallback<ICommentary> mIManagerCallback;

    public static CommentaryManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (CommentaryManager.class) {
                if (sInstance == null) {
                    sInstance = new CommentaryManager(context);
                }
            }
        }
        return sInstance;
    }

    private CommentaryManager(Context pContext) {
        mContext = pContext;
    }

    private void getCommentsForPhoto(){

    }
}
