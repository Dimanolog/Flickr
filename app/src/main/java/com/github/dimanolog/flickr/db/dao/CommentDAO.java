package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.db.dao.cursorwrappers.CommentCursorWrapper;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema.CommentaryTable;
import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

public class CommentDAO extends AbstractDAO<ICommentary> {

    public CommentDAO(Context pContext) {
        super(pContext, Commentary.class);
    }

    public ICustomCursorWrapper<ICommentary> getCommentsByPhoto(IPhoto pPhoto){
        String photoIdStr = String.valueOf(pPhoto.getId());
        return super.query(CommentaryTable.Cols.PHOTO_ID + " = ?", new String[]{photoIdStr});
    }

    @Override
    protected ICustomCursorWrapper<ICommentary> wrapCursor(Cursor pCursor) {
        return new CommentCursorWrapper(pCursor);
    }

    @Override
    protected ContentValues entityToContentValues(ICommentary pEntity) {
        ContentValues values = new ContentValues();

        values.put(CommentaryTable.Cols.ID, pEntity.getId());
        values.put(CommentaryTable.Cols.AUTHOR_NAME, pEntity.getAuthorName());
        values.put(CommentaryTable.Cols.AUTHOR_IS_DELETED, pEntity.getAuthorIsDeleted());
        values.put(CommentaryTable.Cols.ICON_SERVER, pEntity.getIconServer());
        values.put(CommentaryTable.Cols.ICON_FARM, pEntity.getIconFarm());
        values.put(CommentaryTable.Cols.DATE_CREATE, pEntity.getDateCreate());
        values.put(CommentaryTable.Cols.PERMALINK, pEntity.getPermalink());
        values.put(CommentaryTable.Cols.PATH_ALIAS, pEntity.getPathAlias());
        values.put(CommentaryTable.Cols.REAL_NAME, pEntity.getRealName());
        values.put(CommentaryTable.Cols.CONTENT, pEntity.getContent());
        values.put(CommentaryTable.Cols.PHOTO_ID, pEntity.getPhotoID());

        return values;
    }
}
