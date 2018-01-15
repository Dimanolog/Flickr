package com.github.dimanolog.flickr.api;

import android.net.Uri;

public class FlickrApiConstants {
    static final String API_KEY_VALUE = "47e8d1e158478d2fd8c02a85d0350293";
    static final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/rest";
    static final String METHOD_PARAM = "method";
    public static final String SECRET_KEY = "74c86f039fe8aeed";
    static final String NOJSONCALLBACK = "nojsoncallback";
    static final String FORMAT_PARAM = "format";
    static final String FORMAT_VALUE = "json";
    static final String API_KEY_PARAM = "api_key";
    static final Uri FLICKR_API_URL = Uri
            .parse(FLICKR_API_BASE_URL)
            .buildUpon()
            .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
            .appendQueryParameter(FORMAT_PARAM, FORMAT_VALUE)
            .appendQueryParameter(NOJSONCALLBACK, "1")
            .build();

    public static final String FLICKR_BASE_URL = "https://www.flickr.com";



    private FlickrApiConstants() {

    }

}
