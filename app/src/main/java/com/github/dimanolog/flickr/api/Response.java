package com.github.dimanolog.flickr.api;

import com.github.dimanolog.flickr.api.interfaces.IResponse;

/**
 * Created by Dimanolog on 09.01.2018.
 */

public class Response<T> implements IResponse<T> {

    private T mResult;
    private Throwable mThrowable;
    private boolean mIsError;

    public Response() {
    }
    public Response(Throwable mThrowable) {
        this.mThrowable = mThrowable;
        this.mIsError = true;
    }

    public Response(T pResult) {
        mResult = pResult;
    }

    @Override
    public T getResult() {
        return mResult;
    }

    @Override
    public Throwable getError() {
        return mThrowable;
    }

    @Override
    public boolean isError() {
        return mIsError;
    }

    public void setResult(T pResult) {
        mResult = pResult;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable pThrowable) {
        mIsError = true;
        mThrowable = pThrowable;
    }
}
