package com.github.dimanolog.flickr.api;

import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dimanolog on 11.01.2018.
 */

public abstract class AbstractHttpResponseListener<T> implements HttpClient.ResponseListener {
    private IResponse<T> mResponse;

    @Override
    public void onResponse(InputStream pInputStream) throws IOException {
        responseAction(pInputStream);
    }

    @Override
    public void onError(Throwable t) {
        mResponse = new Response<>(t);
    }

    public IResponse<T> getResponse() {
        return mResponse;
    }

    abstract protected void responseAction(InputStream pInputStream);
}
