package com.github.dimanolog.flickr.parsers.photo;

import com.github.dimanolog.flickr.parsers.interfaces.IParser;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public class PhotoParserFactory {

    public IParser getJsonParser() {
        return new JsonPhotoParser();
    }

    public IParser getGsonParser() {
        return new GsonPhotoParser();
    }
}
