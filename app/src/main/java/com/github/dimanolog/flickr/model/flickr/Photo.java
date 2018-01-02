package com.github.dimanolog.flickr.model.flickr;

import android.net.Uri;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.google.gson.annotations.SerializedName;

@Table(FlickrDbSchema.PhotoTable.NAME)
public class Photo implements IPhoto {
    @SerializedName("id")
    @Identity(value = FlickrDbSchema.PhotoTable.Cols.ID, autoincrement = false)
    private Long mId;
    @SerializedName("title")
    @Column(FlickrDbSchema.PhotoTable.Cols.TITLE)
    private String mTittle;
    @SerializedName("url_s")
    @Column(FlickrDbSchema.PhotoTable.Cols.URL)
    private String mUrl;
    @SerializedName("owner")
    @Column(FlickrDbSchema.PhotoTable.Cols.OWNER)
    private String mOwner;
    @SerializedName("dateupload")
    @Column(FlickrDbSchema.PhotoTable.Cols.UPLOAD_DATE)
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
