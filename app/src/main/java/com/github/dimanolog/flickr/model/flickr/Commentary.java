

package com.github.dimanolog.flickr.model.flickr;


import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.google.gson.annotations.SerializedName;

@Table(FlickrDbSchema.CommentaryTable.NAME)
public class Commentary implements ICommentary {
    private static final String USER_AVATAR_URL_TAMPLATE ="http://farm%s.staticflickr.com/%s/buddyicons/%s.jpg";

    @Identity(FlickrDbSchema.CommentaryTable.Cols.ID)
    @SerializedName("id")
    private Long mid;
    @Column(FlickrDbSchema.CommentaryTable.Cols.AUTHOR_NAME)
    @SerializedName("authorname")
    private String mAuthorName;
    @Column(FlickrDbSchema.CommentaryTable.Cols.AUTHOR_IS_DELETED)
    @SerializedName("author_is_deleted")
    private  Integer mAuthorIsDeleted;
    @Column(FlickrDbSchema.CommentaryTable.Cols.ICON_SERVER)
    @SerializedName("iconserver")
    private   Integer mIconServer;
    @Column(FlickrDbSchema.CommentaryTable.Cols.ICON_FARM)
    @SerializedName("iconfarm")
    private   Integer mIconFarm;
    @Column(FlickrDbSchema.CommentaryTable.Cols.DATE_CREATE)
    @SerializedName("datecreate")
    private   Long mDateCreate;
    @Column(FlickrDbSchema.CommentaryTable.Cols.PERMALINK)
    @SerializedName("permalink")
    private  String mPermalink;
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
    public Long getId() {
        return mid;
    }

    @Override
    public void setId(Long pId) {
        mid = pId;
    }

    @Override
    public String getAuthorName() {
        return mAuthorName;
    }

    @Override
    public void setAuthorName(String pAuthorName) {
        mAuthorName = pAuthorName;
    }

    @Override
    public Integer getAuthorIsDeleted() {
        return mAuthorIsDeleted;
    }

    @Override
    public void setAuthorIsDeleted(Integer pAuthorIsDeleted) {
        mAuthorIsDeleted = pAuthorIsDeleted;
    }

    @Override
    public Integer getIconServer() {
        return mIconServer;
    }

    @Override
    public void setIconServer(Integer pIconServer) {
        mIconServer = pIconServer;
    }

    @Override
    public Integer getIconFarm() {
        return mIconFarm;
    }

    @Override
    public void setIconFarm(Integer pIconFarm) {
        mIconFarm = pIconFarm;
    }

    @Override
    public Long getDateCreate() {
        return mDateCreate;
    }

    @Override
    public void setDateCreate(Long pDateCreate) {
        mDateCreate = pDateCreate;
    }

    @Override
    public String getPermalink() {
        return mPermalink;
    }

    @Override
    public void setPermalink(String pPermalink) {
        mPermalink = pPermalink;
    }

    @Override
    public String getPathAlias() {
        return mPathAlias;
    }

    @Override
    public void setPathAlias(String pPathAlias) {
        mPathAlias = pPathAlias;
    }

    @Override
    public String getRealName() {
        return mRealName;
    }

    @Override
    public void setRealName(String pRealName) {
        mRealName = pRealName;
    }

    @Override
    public String getContent() {
        return mContent;
    }

    @Override
    public void setContent(String pContent) {
        mContent = pContent;
    }

    @Override
    public String getAvatarUrl(){
        return String.format(USER_AVATAR_URL_TAMPLATE, mIconFarm, mIconServer, mid);
    }
}

