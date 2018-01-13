package com.github.dimanolog.flickr.dataservice;

import android.content.Context;

import com.github.dimanolog.flickr.db.dao.CommentDAO;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

import java.util.Collection;

public class CommentDataService {
    public static final String TAG = CommentDataService.class.getSimpleName();

    private CommentDAO mCommentDAO;

    public CommentDataService(Context pContext) {
        mCommentDAO = new CommentDAO(pContext);
    }

    public ICustomCursorWrapper<ICommentary> addToDbAndGetCursor(Collection<ICommentary> pComments, IPhoto pPhoto) {
        setPhotoIdsToComments(pComments, pPhoto);
        mCommentDAO.bulkInsert(pComments);

        return mCommentDAO.getCommentsByPhoto(pPhoto);
    }

    private void setPhotoIdsToComments(Collection<ICommentary> pComments, IPhoto pPhoto) {
        for (ICommentary comment : pComments) {
            comment.setId(pPhoto.getId());
        }
    }
}


