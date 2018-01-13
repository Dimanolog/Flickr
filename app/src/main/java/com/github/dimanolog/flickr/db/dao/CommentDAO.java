package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;

/**
 * Created by Dimanolog on 13.01.2018.
 */

public class CommentDAO extends AbstractDAO<ICommentary> {

    protected CommentDAO(Context pContext) {
        super(pContext, Commentary.class);
    }

    @Override
    protected ICustomCursorWrapper<ICommentary> wrapCursor(Cursor pCursor) {
        return null;
    }

    @Override
    protected ContentValues entityToContentValues(ICommentary pEntity) {
        return null;
    }
}
