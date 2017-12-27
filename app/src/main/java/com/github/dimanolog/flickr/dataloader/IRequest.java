package com.github.dimanolog.flickr.dataloader;

/**
 * Created by Dimanolog on 06.11.2017.
 */

interface IRequest {
    void onPreRequest();

    void runRequest();

    void onPostRequest();
}
