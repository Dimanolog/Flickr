package com.github.dimanolog.flickr.parsers.photo;

import com.github.dimanolog.flickr.model.Photo;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public class GsonPhotoParser implements IParser<Photo> {

    private final Gson mGson;

    GsonPhotoParser() {
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public Photo parseObject(String jsonString) {
        return mGson.fromJson(jsonString, Photo.class);
    }

    @Override
    public List<Photo> parseArray(String jsonArray) {
        Photo[] photos = mGson.fromJson(jsonArray, Photo[].class);
        return Arrays.asList(photos);
    }
}
