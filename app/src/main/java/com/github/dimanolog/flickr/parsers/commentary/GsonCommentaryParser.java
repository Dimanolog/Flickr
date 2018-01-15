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

package com.github.dimanolog.flickr.parsers.commentary;

import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GsonCommentaryParser implements IParser<ICommentary> {

    private static final String COMMENTS = "comments";
    private static final String COMMENT = "comment";
    private final Gson mGson;

    GsonCommentaryParser() {
        mGson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public ICommentary parseObject(String pJsonString) {
        return mGson.fromJson(pJsonString, Commentary.class);
    }

    @Override
    public List<ICommentary> parseArray(String pJsonString) {
        JsonParser parser = new JsonParser();
        JsonObject jsonBody = parser.parse(pJsonString).getAsJsonObject();
        JsonObject jsonObjComments = jsonBody.getAsJsonObject(COMMENTS);
        JsonArray jsonArrayComments = jsonObjComments.getAsJsonArray(COMMENT);
        ICommentary[] commentaries = mGson.fromJson(jsonArrayComments, Commentary[].class);
        if (commentaries != null) {
            return Arrays.asList(commentaries);
        } else {
            return Collections.emptyList();
        }
    }
}
