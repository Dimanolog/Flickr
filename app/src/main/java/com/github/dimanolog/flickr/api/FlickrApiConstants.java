package com.github.dimanolog.flickr.api;

import android.net.Uri;

class FlickrApiConstants {
    static final String API_KEY = "47e8d1e158478d2fd8c02a85d0350293";
    static final String SECRET_KEY = "74c86f039fe8aeed";
    public static final String FLICKR_BASE_URL = "https://www.flickr.com";
    static final String FLICKR_API_BASE_URL = "https://api.flickr.com/services/rest";
    static final Uri FLICKR_API_URL = Uri
            .parse(FLICKR_API_BASE_URL)
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")

            .appendQueryParameter("nojsoncallback", "1")
            .build();

    static final String METHOD_PARAM = "method";


    private FlickrApiConstants() {

    }

}
