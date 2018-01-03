package com.github.dimanolog.flickr.model.flickr.interfaces;

import android.net.Uri;


public interface IPhoto {
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

    public String getOriginalUrl();

    public void setOriginalUrl(String pOriginalUrl);
}
