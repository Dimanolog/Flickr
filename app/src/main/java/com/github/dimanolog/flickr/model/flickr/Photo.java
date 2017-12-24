package com.github.dimanolog.flickr.model.flickr;

import android.net.Uri;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.google.gson.annotations.SerializedName;
@Table("photo")
public class Photo implements IPhoto {
    @SerializedName("id")
    @Identity("_id")
    private Long mId;
    @SerializedName("title")
    @Column("tittle")
    private String mTittle;
    @Column("url_s")
    @SerializedName("url_s")
    private String mUrl;
    @Column("owner")
    @SerializedName("owner")
    private String mOwner;
    @Column("date_upload")
    @SerializedName("dateupload")
    private Long uploadDate;

    @Override
    public Long getId() {
        return mId;
    }

    @Override
    public void setId(Long pId) {
        mId = pId;
    }

    @Override
    public String getTittle() {
        return mTittle;
    }

    @Override
    public void setTittle(String pTittle) {
        mTittle = pTittle;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(String pUrl) {
        mUrl = pUrl;
    }

    @Override
    public String getOwner() {
        return mOwner;
    }

    @Override
    public void setOwner(String pOwner) {
        mOwner = pOwner;
    }

    @Override
    public Long getUploadDate() {
        return uploadDate;
    }

    @Override
    public void setUploadDate(Long pUploadDate) {
        uploadDate = pUploadDate;
    }

    @Override
    public Uri getPhotoPageUri() {
        return Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(mOwner)
                .appendPath(mId.toString())
                .build();
    }


}
