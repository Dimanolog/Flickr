package com.github.dimanolog.flickr.datamanagers;

import com.github.dimanolog.flickr.api.interfaces.IResponse;

public abstract class AbstractFlickrRequest<Response, Callback> implements IRequest {
    private IResponse<Response> mResponse;
    private IManagerCallback<Callback> mManagerCallback;

    public AbstractFlickrRequest(IResponse<Response> pResponse, IManagerCallback<Callback> pManagerCallback) {
        mResponse = pResponse;
        mManagerCallback = pManagerCallback;
    }

    @Override
    public void onPreRequest() {
        mManagerCallback.onStartLoading();
    }

    @Override
    public void runRequest() {
        onRunRequest();
    }

    @Override
    public void onPostRequest() {
        if(mResponse.isError()){
            mManagerCallback.onError(mResponse.getError());
        }

    }

     protected abstract void onRunRequest();
}
