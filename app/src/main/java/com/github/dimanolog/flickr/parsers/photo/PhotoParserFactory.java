package com.github.dimanolog.flickr.parsers.photo;

import com.github.dimanolog.flickr.model.Flickr.IPhoto;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public class PhotoParserFactory {

    public IParser<IPhoto> getJsonParser() {
        return new JsonPhotoParser();
    }
    
    public IParser<IPhoto> getGsonParser() {
        return new GsonPhotoParser();
    }
}
