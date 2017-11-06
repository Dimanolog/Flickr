package com.github.dimanolog.flickr.model.flickr;

import android.net.Uri;

import java.util.Date;

/**
 * Created by Dimanolog on 15.10.2017.
 */

public interface IPhoto {
    String getOwner();

    void setOwner(String owner);

    Long getId();

    void setId(Long id);

    String getCaption();

    void setCaption(String caption);

    String getUrl();

    void setUrl(String url);

    Uri getPhotoPageUri();

    Date getUploadDate();

    void setUploadDate(Date uploadDate);
}
