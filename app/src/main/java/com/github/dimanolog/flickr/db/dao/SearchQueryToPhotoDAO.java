package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.SearchQueryToPhoto;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dimanolog on 02.01.2018.
 */

public class SearchQueryToPhotoDAO extends AbstractDAO<SearchQueryToPhoto> {
    public SearchQueryToPhotoDAO(Context pContext) {
        super(pContext, SearchQueryToPhoto.class);
    }

    @Override
    protected ICustomCursorWrapper<SearchQueryToPhoto> wrapCursor(Cursor pCursor) {
        return null;
    }

    @Override
    protected ContentValues entityToContentValues(SearchQueryToPhoto pEntity) {
        return null;
    }

    public Integer addConnection(Collection<IPhoto> pIPhotos, Long pSearchQueryId) {
        ContentValues values;
        List<ContentValues> valuesList = new LinkedList<>();
        for (IPhoto photo : pIPhotos) {
            values=new ContentValues();
            values.put(FlickrDbSchema.SearchQueryToPhotoTable.Cols.photoId, photo.getId());
            values.put(FlickrDbSchema.SearchQueryToPhotoTable.Cols.searchQueryId, pSearchQueryId);
            valuesList.add(values);
        }
        if (!valuesList.isEmpty()) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            int count = 0;
            try {
                db.beginTransaction();

                for (final ContentValues value : valuesList) {
                    db.insert(FlickrDbSchema.SearchQueryToPhotoTable.NAME, null, value);
                    count++;
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return count;
        }
        return null;
    }
}
