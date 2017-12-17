package com.github.dimanolog.flickr.model.flickr;

import android.net.Uri;

import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.github.dimanolog.flickr.db.annotations.Column;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
@Table("Photo")
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
    private Date uploadDate;

    @Override
    public String getOwner() {
        return mOwner;
    }

    @Override
    public void setOwner(String owner) {
        mOwner = owner;
    }

    @Override
    public Long getId() {
        return mId;
    }

    @Override
    public void setId(Long id) {
        mId = id;
    }

    @Override
    public String getTittle() {
        return mTittle;
    }

    @Override
    public void setTittle(String caption) {
        mTittle = caption;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }
    @Override
    public String toString() {
        if (mTittle.length() > 15)
            return mTittle.substring(0, 15) + "...";
        else
            return mTittle;
    }

    @Override
    public Uri getPhotoPageUri() {
        return Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(mOwner)
                .appendPath(mId.toString())
                .build();
    }

    @Override
    public Date getUploadDate() {
        return uploadDate;
    }

    @Override
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
