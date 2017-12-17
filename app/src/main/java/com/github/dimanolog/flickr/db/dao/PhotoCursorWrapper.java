package com.github.dimanolog.flickr.db.dao;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.dimanolog.flickr.db.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.model.flickr.Photo;

import java.util.Date;

/**
 * Created by Dimanolog on 17.12.2017.
 */

public class PhotoCursorWrapper extends CursorWrapper implements ICustomCursorWrapper<IPhoto> {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    @Override
    public IPhoto get() {
        Long id = getLong(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.ID));
        String tittle = getString(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.TITLE));
        Date uploadDate = new Date(getLong(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.UPLOAD_DATE)));
        String owner = getString(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.OWNER));
        String url = getString(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.OWNER));

        IPhoto photo = new Photo();
        photo.setTittle(tittle);
        photo.setId(id);
        photo.setOwner(owner);
        photo.setUploadDate(uploadDate);
        photo.setUrl(url);

        return photo;
    }
}
