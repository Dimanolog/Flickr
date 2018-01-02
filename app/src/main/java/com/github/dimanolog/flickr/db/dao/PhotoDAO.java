package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.PhotoCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.Photo;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.model.flickr.interfaces.ISearchQuery;

import static com.github.dimanolog.flickr.db.schema.FlickrDbSchema.PhotoTable;
import static com.github.dimanolog.flickr.db.schema.FlickrDbSchema.SearchQueryToPhotoTable;


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
        values.put(PhotoTable.Cols.ID, pEntity.getId());
        values.put(PhotoTable.Cols.OWNER, pEntity.getOwner());
        values.put(PhotoTable.Cols.TITLE, pEntity.getTittle());
        values.put(PhotoTable.Cols.UPLOAD_DATE, pEntity.getUploadDate());
        values.put(PhotoTable.Cols.URL, pEntity.getUrl());

        return values;
    }

    public ICustomCursorWrapper<IPhoto> getPhotosBySearch(ISearchQuery pSearchQuery) {
        String s = new StringBuilder()
                .append("SELECT * FROM ")
                .append(PhotoTable.NAME)
                .append("WHERE ")
                .append(PhotoTable.Cols.ID)
                .append("IN ( SELECT ")
                .append(SearchQueryToPhotoTable.Cols.photoId)
                .append(" FROM ").append(SearchQueryToPhotoTable.NAME)
                .append(" WHERE ").append(SearchQueryToPhotoTable.Cols.searchQueryId)
                .append("=")
                .append(pSearchQuery.getId())
                .append(" )").toString();

       return rawQuery(s,null);
    }
}
