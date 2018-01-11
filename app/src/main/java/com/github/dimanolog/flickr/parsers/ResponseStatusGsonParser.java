package com.github.dimanolog.flickr.parsers;

import com.github.dimanolog.flickr.api.IResponseStatus;
import com.github.dimanolog.flickr.api.ResponseStatus;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ResponseStatusGsonParser implements IParser<IResponseStatus> {
    private final Gson mGson;

    ResponseStatusGsonParser() {
        mGson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public IResponseStatus parseObject(String pJsonString) {
        return mGson.fromJson(pJsonString, ResponseStatus.class);
    }

    @Override
    public List<IResponseStatus> parseArray(String pJsonString) {
        throw new UnsupportedOperationException("this not supported");
    }
}
