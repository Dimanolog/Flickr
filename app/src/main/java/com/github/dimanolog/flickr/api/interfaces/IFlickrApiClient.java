package com.github.dimanolog.flickr.api.interfaces;

import com.github.dimanolog.flickr.model.flickr.IPhoto;

import java.util.List;

/**
 * Created by Dimanolog on 04.11.2017.
 */

public interface IFlickrApiClient {
    public List<IPhoto> getRecent(int page);

    public List<IPhoto> searchPhotos(int page, String search);
}
