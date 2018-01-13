package com.github.dimanolog.flickr.model.flickr;

import android.net.Uri;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.google.gson.annotations.SerializedName;

import static com.github.dimanolog.flickr.db.schema.FlickrDbSchema.PhotoTable;

//{"photos":{"page":1,"pages":3812,"perpage":100,"total":"381192","photo":
// [{"id":"38772749375",
// "owner":"137903414@N03",
// "secret":"70b6dc4d31",
// "server":"4707",
// "farm":5,"
// title": "Faulty Destination Display | Stagecoach Cambridge [AE10 BXA] 19589",
// "ispublic":1,
// "isfriend":0,
// "isfamily":0,
// "dateupload":"1515869448",
// "count_faves":"0",
// "count_comments":"0",
// "url_z":
// "https:\/\/farm5.staticflickr.com\/4707\/38772749375_70b6dc4d31_z.jpg",
// "height_z":"427",
// "width_z":"640",
// "url_o":
// "https:\/\/farm5.staticflickr.com\/4707\/38772749375_c9f1bf248d_o.jpg",
// "height_o":"3456",
// "width_o":"5184"}
@Table(PhotoTable.NAME)
public class Photo implements IPhoto {
    @SerializedName("id")
    @Identity(value = PhotoTable.Cols.ID)
    private Long mId;
    @SerializedName("title")
    @Column(PhotoTable.Cols.TITLE)
    private String mTittle;
    @SerializedName("url_z")
    @Column(PhotoTable.Cols.SMALL_IMAGE_URL)
    private String mSmallUrl;
    @SerializedName("url_o")
    @Column("original_url")
    private String mOriginalUrl;
    @SerializedName("owner")
    @Column(PhotoTable.Cols.OWNER)
    private String mOwner;
    @SerializedName("dateupload")
    @Column(PhotoTable.Cols.UPLOAD_DATE)
    private Long uploadDate;
    @Column(PhotoTable.Cols.COUNT_FAVES)
    @SerializedName("count_faves")
    private Integer countFaves;
    @Column(PhotoTable.Cols.COUNT_COMMENTS)
    @SerializedName("count_comments")
    private Integer countComments;

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
    public String getSmallUrl() {
        return mSmallUrl;
    }

    @Override
    public void setSmallUrl(String pSmallUrl) {
        mSmallUrl = pSmallUrl;
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
    public String getOriginalUrl() {
        return mOriginalUrl;
    }
    @Override
    public void setOriginalUrl(String pOriginalUrl) {
        mOriginalUrl = pOriginalUrl;
    }
    @Override
    public Integer getCountFaves() {
        return countFaves;
    }
    @Override
    public void setCountFaves(Integer pCountFaves) {
        countFaves = pCountFaves;
    }
    @Override
    public Integer getCountComments() {
        return countComments;
    }
    @Override
    public void setCountComments(Integer pCountComments) {
        countComments = pCountComments;
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
