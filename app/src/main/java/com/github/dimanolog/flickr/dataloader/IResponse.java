package com.github.dimanolog.flickr.dataloader;

/**
 * Created by Dimanolog on 19.11.2017.
 */

public interface IResponse<T> {
    T getResult();
    Throwable getError();
    boolean isError();
}
