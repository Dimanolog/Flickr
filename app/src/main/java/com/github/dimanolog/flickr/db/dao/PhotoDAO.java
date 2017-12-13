package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.model.flickr.Photo;

/**
 * Created by Dimanolog on 10.12.2017.
 */

public class PhotoDAO extends AbstractDAO<Photo> {

    PhotoDAO(Context pContext) {
        super(pContext, Photo.class);
    }

    @Override
    protected Photo cursorToEntity(Cursor pCursor) {
        return null;
    }

    @Override
    protected ContentValues entityToContentValues(Photo pEntity) {
        return null;
    }
}
