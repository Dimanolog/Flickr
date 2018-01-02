package com.github.dimanolog.flickr.db.dao.cursorwrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.Photo;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

/**
 * Created by Dimanolog on 17.12.2017.
 */

public class PhotoCursorWrapper extends CursorWrapper implements ICustomCursorWrapper<IPhoto> {

    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    @Override
    public IPhoto get() {
        Long id = getLong(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.ID));
        String tittle = getString(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.TITLE));
        Long uploadDate =getLong(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.UPLOAD_DATE));
        String owner = getString(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.OWNER));
        String url = getString(getColumnIndex(FlickrDbSchema.PhotoTable.Cols.URL));

        IPhoto photo = new Photo();
        photo.setTittle(tittle);
        photo.setId(id);
        photo.setOwner(owner);
        photo.setUploadDate(uploadDate);
        photo.setUrl(url);

        return photo;
    }
}
