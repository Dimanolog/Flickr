package com.github.dimanolog.flickr.parsers.photo;

import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.model.flickr.Photo;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GsonPhotoParser implements IParser<IPhoto> {

    private final Gson mGson;

    GsonPhotoParser() {
       GsonBuilder gsonBuilder= new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
        mGson = gsonBuilder.create();
    }

    @Override
    public IPhoto parseObject(String jsonString) {
        return mGson.fromJson(jsonString, Photo.class);
    }

    @Override
    public List<IPhoto> parseArray(String jsonArray) {
        IPhoto[] photos = mGson.fromJson(jsonArray, Photo[].class);
        return Arrays.asList(photos);
    }
}
