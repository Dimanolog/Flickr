

package com.github.dimanolog.flickr.model.flickr;


import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.google.gson.annotations.SerializedName;

public class Commentary implements ICommentary {
    private static final String USER_AVATAR_URL_TAMPLATE =
            "http://farm%s.staticflickr.com/%s/buddyicons/%s.jpg";

    @SerializedName("id")
    private Long mid;
    @SerializedName("authorname")
    private String mAuthorName;
    @SerializedName("author_is_deleted")
    private  Integer mAuthorIsDeleted;
    @SerializedName("iconserver")
    private   Integer mIconServer;
    @SerializedName("iconfarm")
    private   Integer mIconFarm;
    @SerializedName("datecreate")
    private   Long mDateCreate;
    @SerializedName("permalink")
    private  String mPermalink;
    @SerializedName("path_alias")
    private String mPathAlias;
    @SerializedName("realname")
    private String mRealName;
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

