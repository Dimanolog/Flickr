package com.github.dimanolog.flickr.datamanagers.comment;

import com.github.dimanolog.flickr.api.FlickrApiCommentaryClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.datamanagers.IManagerCallback;
import com.github.dimanolog.flickr.datamanagers.IRequest;
import com.github.dimanolog.flickr.dataservice.CommentDataService;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

import java.util.List;

public class CommentsForPhotoRequest implements IRequest {
    private IManagerCallback<ICustomCursorWrapper<ICommentary>> mCommentManagerCallback;
    private FlickrApiCommentaryClient mFlickrApiCommentaryClient;
    private IResponse<List<ICommentary>> mCommentResponse;
    private IPhoto mPhoto;
    private CommentDataService mCommentDataService;
    private ICustomCursorWrapper<ICommentary> mCommentCursorWrapper;

    public CommentsForPhotoRequest(IManagerCallback<ICustomCursorWrapper<ICommentary>> pCommentManagerCallback,
                                   FlickrApiCommentaryClient pFlickrApiCommentaryClient,
                                   IPhoto pPhoto, CommentDataService pCommentDataService) {

        mCommentManagerCallback = pCommentManagerCallback;
        mFlickrApiCommentaryClient = pFlickrApiCommentaryClient;
        mPhoto = pPhoto;
        mCommentDataService = pCommentDataService;
    }

    @Override
    public void onPreRequest() {
        mCommentManagerCallback.onStartLoading();
    }

    @Override
    public void runRequest() {
        mCommentResponse = mFlickrApiCommentaryClient.getListOfCommentByPhoto(mPhoto);
        if (!mCommentResponse.isError()) {
            mCommentCursorWrapper = mCommentDataService.addToDbAndGetCursor(mCommentResponse.getResult(), mPhoto);
        }
    }

    @Override
    public void onPostRequest() {
        if (mCommentResponse.isError()) {
            mCommentManagerCallback.onError(mCommentResponse.getError());
        } else {
            mCommentManagerCallback.onSuccessResult(mCommentCursorWrapper);
        }
    }
}
