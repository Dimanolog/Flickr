package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.model.flickr.Photo;

/**
 * Created by Dimanolog on 10.12.2017.
 */

public class PhotoDAO extends AbstractDAO<IPhoto> {

    public PhotoDAO(Context pContext) {
        super(pContext, Photo.class);
    }

    @Override
    protected ICustomCursorWrapper<IPhoto> wrapCursor(Cursor pCursor) {
        return new PhotoCursorWrapper(pCursor);
    }

    @Override
    protected ContentValues entityToContentValues(IPhoto pEntity) {
        ContentValues values = new ContentValues();
        values.put(FlickrDbSchema.PhotoTable.Cols.ID, pEntity.getId());
        values.put(FlickrDbSchema.PhotoTable.Cols.OWNER, pEntity.getOwner());
        values.put(FlickrDbSchema.PhotoTable.Cols.TITLE, pEntity.getTittle());
        values.put(FlickrDbSchema.PhotoTable.Cols.UPLOAD_DATE, pEntity.getUploadDate());
        values.put(FlickrDbSchema.PhotoTable.Cols.URL, pEntity.getUrl());

        return values;
    }
}
