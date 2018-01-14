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
import com.github.dimanolog.flickr.api.IResponseStatus;
import com.github.dimanolog.flickr.api.ResponseStatus;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.datamanagers.AutheficationManager;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.IRequest;
import com.github.dimanolog.flickr.datamanagers.UserSession;
import com.github.dimanolog.flickr.dataservice.CommentDataService;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.threading.RequestExecutor;

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

    public void addCommentToPhoto(final IPhoto pPhoto, final String pComment, final IManagerCallback<IResponseStatus> pManagerCallback){
        final UserSession userSession = AutheficationManager.getInstance(mContext)
                .getUserSession();
        IRequest addCommentRequest = new IRequest() {
            private IResponse<ResponseStatus> mResponseStatusResponse;
            @Override
            public void onPreRequest() {
                pManagerCallback.onStartLoading();
            }
            @Override
            public void runRequest() {
                mResponseStatusResponse = mFlickrApiCommentaryClient.addComment(pPhoto.getId(), pComment, userSession);

            }
            @Override
            public void onPostRequest() {
                if(mResponseStatusResponse.isError()) {
                    pManagerCallback.onError(mResponseStatusResponse.getError());
                }
               else {
                    pManagerCallback.onSuccessResult(mResponseStatusResponse.getResult());
                }
            }
        };
        RequestExecutor.executeRequestSerial(addCommentRequest);
    }

}
