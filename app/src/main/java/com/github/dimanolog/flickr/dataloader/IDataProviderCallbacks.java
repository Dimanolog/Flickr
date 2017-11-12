package com.github.dimanolog.flickr.dataloader;

/**
 * Created by Dimanolog on 12.11.2017.
 */

public interface IDataProviderCallbacks<T> {
    void onStartLoading();
    void onSuccessResult(T result);
    void onError(Throwable t);
}
