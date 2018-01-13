package com.github.dimanolog.flickr.db.dao.cursorwrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.google.gson.annotations.SerializedName;

public class CommentCursorWrapper extends CursorWrapper implements ICustomCursorWrapper<ICommentary> {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CommentCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    @Identity(FlickrDbSchema.CommentaryTable.Cols.ID)
    @SerializedName("id")
    private Long mid;
    @Column(FlickrDbSchema.CommentaryTable.Cols.AUTHOR_NAME)
    @SerializedName("authorname")
    private String mAuthorName;
    @Column(FlickrDbSchema.CommentaryTable.Cols.AUTHOR_IS_DELETED)
    @SerializedName("author_is_deleted")
    private Integer mAuthorIsDeleted;
    @Column(FlickrDbSchema.CommentaryTable.Cols.ICON_SERVER)
    @SerializedName("iconserver")
    private Integer mIconServer;
    @Column(FlickrDbSchema.CommentaryTable.Cols.ICON_FARM)
    @SerializedName("iconfarm")
    private Integer mIconFarm;
    @Column(FlickrDbSchema.CommentaryTable.Cols.DATE_CREATE)
    @SerializedName("datecreate")
    private Long mDateCreate;
    @Column(FlickrDbSchema.CommentaryTable.Cols.PERMALINK)
    @SerializedName("permalink")
    private String mPermalink;
    @Column(FlickrDbSchema.CommentaryTable.Cols.PATH_ALIAS)
    @SerializedName("path_alias")
    private String mPathAlias;
    @Column(FlickrDbSchema.CommentaryTable.Cols.REAL_NAME)
    @SerializedName("realname")
    private String mRealName;
    @Column(FlickrDbSchema.CommentaryTable.Cols.CONTENT)
    @SerializedName("_content")
    private String mContent;

    @Override
    public ICommentary get() {
        ICommentary commentary = new Commentary();
        commentary.setId(getLong(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.ID)));
        commentary.setAuthorName(getString(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.AUTHOR_NAME)));
        commentary.setAuthorIsDeleted(getInt(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.AUTHOR_IS_DELETED)));
        commentary.setIconServer(getInt(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.ICON_SERVER)));
        commentary.setIconFarm(getInt(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.ICON_FARM)));
        commentary.setDateCreate(getLong(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.DATE_CREATE)));
        commentary.setPermalink(getString(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.PERMALINK)));
        commentary.setPathAlias(getString(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.PATH_ALIAS)));
        commentary.setRealName(getString(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.REAL_NAME)));
        commentary.setContent(getString(getColumnIndex(FlickrDbSchema.CommentaryTable.Cols.CONTENT)));

        return commentary;
    }
}
