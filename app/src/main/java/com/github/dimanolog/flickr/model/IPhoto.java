package com.github.dimanolog.flickr.model;

import android.net.Uri;

/**
 * Created by Dimanolog on 15.10.2017.
 */

interface IPhoto {
    String getOwner();

    void setOwner(String owner);

    String getId();

    void setId(String id);

    String getCaption();

    void setCaption(String caption);

    String getUrl();

    void setUrl(String url);

    Uri getPhotoPageUri();
}
