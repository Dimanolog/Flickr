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

package com.github.dimanolog.flickr.datamanagers.comment;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiCommentaryClient;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.IRequest;
import com.github.dimanolog.flickr.dataservice.CommentDataService;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.threading.RequestExecutor;

/**
 * Created by dimanolog on 11.01.18.
 */

public class CommentsManager {
    private static CommentsManager sInstance;
    private Context mContext;
    private IManagerCallback<ICustomCursorWrapper<ICommentary>> mCommenataryManagerCallback;
    private FlickrApiCommentaryClient mFlickrApiCommentaryClient;
    private CommentDataService mCommentDataService;

    public static CommentsManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (CommentsManager.class) {
                if (sInstance == null) {
                    sInstance = new CommentsManager(context);
                }
            }
        }
        return sInstance;
    }

    private CommentsManager(Context pContext) {
        mContext = pContext.getApplicationContext();
        mCommentDataService = new CommentDataService(pContext);
        mFlickrApiCommentaryClient = new FlickrApiCommentaryClient();
    }

    public void registerCallback(IManagerCallback<ICustomCursorWrapper<ICommentary>> pCommentaryManagerCallback) {
        mCommenataryManagerCallback = pCommentaryManagerCallback;
    }

    public void getCommentsForPhoto(IPhoto pPhotoId) {
        IRequest commentsRequest = new CommentsForPhotoRequest(mCommenataryManagerCallback,
                mFlickrApiCommentaryClient, pPhotoId, mCommentDataService);

        RequestExecutor.executeRequestSerial(commentsRequest);

    }

}
