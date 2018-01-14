package com.github.dimanolog.flickr.db.dao.cursorwrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.dimanolog.flickr.db.schema.FlickrDbSchema.CommentaryTable;
import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;

public class CommentCursorWrapper extends CursorWrapper implements ICustomCursorWrapper<ICommentary> {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CommentCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    @Override
    public ICommentary get() {
        ICommentary commentary = new Commentary();
        commentary.setId(getString(getColumnIndex(CommentaryTable.Cols.ID)));
        commentary.setAuthorName(getString(getColumnIndex(CommentaryTable.Cols.AUTHOR_NAME)));
        commentary.setAuthorIsDeleted(getInt(getColumnIndex(CommentaryTable.Cols.AUTHOR_IS_DELETED)));
        commentary.setIconServer(getInt(getColumnIndex(CommentaryTable.Cols.ICON_SERVER)));
        commentary.setIconFarm(getInt(getColumnIndex(CommentaryTable.Cols.ICON_FARM)));
        commentary.setDateCreate(getLong(getColumnIndex(CommentaryTable.Cols.DATE_CREATE)));
        commentary.setPermalink(getString(getColumnIndex(CommentaryTable.Cols.PERMALINK)));
        commentary.setPathAlias(getString(getColumnIndex(CommentaryTable.Cols.PATH_ALIAS)));
        commentary.setRealName(getString(getColumnIndex(CommentaryTable.Cols.REAL_NAME)));
        commentary.setContent(getString(getColumnIndex(CommentaryTable.Cols.CONTENT)));
        commentary.setAuthorID(getString(getColumnIndex(CommentaryTable.Cols.AUTHOR_ID)));

        return commentary;
    }
}
