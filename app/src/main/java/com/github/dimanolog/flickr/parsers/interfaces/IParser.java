package com.github.dimanolog.flickr.parsers.interfaces;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public interface IParser<T> {
     T parseObject(String jsonString) throws JSONException;
     List<T> parseArray(String jsonArray) throws JSONException;
}
