package com.github.dimanolog.flickr.parsers.photo;

import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;

public class PhotoParserFactory {

    public IParser<IPhoto> getGsonParser() {
        return new GsonPhotoParser();
    }
}
