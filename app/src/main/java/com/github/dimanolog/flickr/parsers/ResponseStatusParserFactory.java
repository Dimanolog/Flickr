package com.github.dimanolog.flickr.parsers;

import com.github.dimanolog.flickr.api.IResponseStatus;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;

/**
 * Created by Dimanolog on 11.01.2018.
 */

public class ResponseStatusParserFactory {

    public IParser<IResponseStatus> getGsonParser() {
        return new ResponseStatusGsonParser();
    }
}
