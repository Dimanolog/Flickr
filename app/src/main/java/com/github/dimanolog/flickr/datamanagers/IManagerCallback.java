package com.github.dimanolog.flickr.datamanagers;

/**
 * Created by Dimanolog on 12.11.2017.
 */

public interface IManagerCallback<T> {
    void onStartLoading();

    void onSuccessResult(T result);

    void onError(Throwable t);
}
