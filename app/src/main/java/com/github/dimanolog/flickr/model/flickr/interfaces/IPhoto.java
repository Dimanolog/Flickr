package com.github.dimanolog.flickr.model.flickr.interfaces;

import android.net.Uri;

import java.io.Serializable;


public interface IPhoto extends Serializable {
    String getOwner();

    void setOwner(String owner);

    Long getId();

    void setId(Long id);

    String getTittle();

    void setTittle(String caption);

    String getSmallUrl();

    void setSmallUrl(String url);

    Uri getPhotoPageUri();

    Long getUploadDate();

    void setUploadDate(Long uploadDate);

    String getOriginalUrl();

    void setOriginalUrl(String pOriginalUrl);

    Integer getCountComments();

    void setCountComments(Integer pCountComments);

    Integer getCountFaves();

    void setCountFaves(Integer pCountFaves);
}
