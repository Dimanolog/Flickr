package com.github.dimanolog.flickr.datamanagers;

/**
 * Created by Dimanolog on 06.11.2017.
 */

public interface IRequest {
    void onPreRequest();

    void runRequest();

    void onPostRequest();
}
