package com.github.dimanolog.flickr.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public class GsonUtil {

    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<T>() {
        }.getType());
    }
}
