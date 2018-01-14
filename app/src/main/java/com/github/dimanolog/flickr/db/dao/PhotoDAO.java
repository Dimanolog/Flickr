package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.PhotoCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.Photo;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.util.LogUtil;

import static com.github.dimanolog.flickr.db.schema.FlickrDbSchema.PhotoTable;
import static com.github.dimanolog.flickr.db.schema.FlickrDbSchema.SearchQueryToPhotoTable;


/**
 * Created by Dimanolog on 10.12.2017.
 */

public class PhotoDAO extends AbstractDAO<IPhoto> {
    private static final String TAG = PhotoDAO.class.getSimpleName();

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
        values.put(PhotoTable.Cols.SMALL_IMAGE_URL, pEntity.getSmallUrl());
        values.put(PhotoTable.Cols.ORIGINAL_IMAGE_URL, pEntity.getOriginalUrl());
        values.put(PhotoTable.Cols.COUNT_COMMENTS, pEntity.getCountComments());
        values.put(PhotoTable.Cols.COUNT_FAVES,pEntity.getCountFaves());

        return values;
    }

    public ICustomCursorWrapper<IPhoto> getPhotosBySearchId(long id, String pOrderByColumn, String pOrderType) {
        String s = "SELECT * FROM " +
                PhotoTable.NAME +
                " WHERE " + PhotoTable.Cols.ID +
                " IN ( SELECT " +
                SearchQueryToPhotoTable.Cols.PHOTO_ID +
                " FROM " + SearchQueryToPhotoTable.NAME +
                " WHERE " + SearchQueryToPhotoTable.Cols.SEARCH_QUERY_ID +
                "=" +  id +" )" +
                " ORDER BY " + pOrderByColumn + " " +pOrderType;

        LogUtil.d(TAG, "getPhotosBySearchId: " + s);
        return rawQuery(s, null);
    }
}
