package com.github.dimanolog.flickr.dataloader;

/**
 * Created by Dimanolog on 06.11.2017.
 */

interface IRequest<T, Y> {
    void onPreExecute();

    T runRequest();

    void onPostExecute(T object);
}
