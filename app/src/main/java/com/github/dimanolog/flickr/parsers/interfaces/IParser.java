package com.github.dimanolog.flickr.parsers.interfaces;

import java.util.List;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public interface IParser<T> {
     T parseObject(String jsonString);
     List<T> parseArray(String jsonArray);
}
