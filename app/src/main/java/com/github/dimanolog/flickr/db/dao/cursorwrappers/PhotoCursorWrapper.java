package com.github.dimanolog.flickr.db.dao.cursorwrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.dimanolog.flickr.model.flickr.Photo;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

import static com.github.dimanolog.flickr.db.schema.FlickrDbSchema.PhotoTable;

public class PhotoCursorWrapper extends CursorWrapper implements ICustomCursorWrapper<IPhoto> {

    public PhotoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    @Override
    public IPhoto get() {
        Long id = getLong(getColumnIndex(PhotoTable.Cols.ID));
        String tittle = getString(getColumnIndex(PhotoTable.Cols.TITLE));
        Long uploadDate = getLong(getColumnIndex(PhotoTable.Cols.UPLOAD_DATE));
        String owner = getString(getColumnIndex(PhotoTable.Cols.OWNER));
        String urlSmall = getString(getColumnIndex(PhotoTable.Cols.SMALL_IMAGE_URL));
        String urlOriginal = getString(getColumnIndex(PhotoTable.Cols.ORIGINAL_IMAGE_URL));
        Integer countFaves = getInt(getColumnIndex(PhotoTable.Cols.COUNT_FAVES));
        Integer countComments = getInt(getColumnIndex(PhotoTable.Cols.COUNT_COMMENTS));

        IPhoto photo = new Photo();
        photo.setTittle(tittle);
        photo.setId(id);
        photo.setOwner(owner);
        photo.setUploadDate(uploadDate);
        photo.setSmallUrl(urlSmall);
        photo.setOriginalUrl(urlOriginal);
        photo.setCountFaves(countFaves);
        photo.setCountComments(countComments);

        return photo;
    }
}
