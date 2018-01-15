package com.github.dimanolog.flickr.api.interfaces;

import android.support.annotation.WorkerThread;

import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;

import java.util.List;

/**
 * Created by Dimanolog on 15.01.2018.
 */
public interface IFlickrApiCommentaryClient {
    @WorkerThread
    IResponse<List<ICommentary>> getListOfCommentByPhoto(IPhoto pPhoto);

    @WorkerThread
    IResponse<IResponseStatus> addComment(Long pPhotoId, String pCommentText, UserSession pUserSession);
}
