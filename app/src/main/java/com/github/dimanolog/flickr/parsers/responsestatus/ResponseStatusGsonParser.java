/*
 * Copyright (c) 2018 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.parsers.responsestatus;

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
