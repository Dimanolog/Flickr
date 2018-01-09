package com.github.dimanolog.flickr.api;

import android.net.Uri;


class FlickrApiConstants {
    static final String API_KEY = "47e8d1e158478d2fd8c02a85d0350293";
    public static final String FLICKR_BASE_URL = "https://www.flickr.com";
    static final String FLICKR_API_BASE_URL = "https://api.flickr.com/services";
    static final String SECRET_KEY = "74c86f039fe8aeed";

    static final Uri ENDPOINT = Uri
            .parse(FLICKR_API_BASE_URL)
            .buildUpon()
            .appendPath("rest")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "date_upload,url_z,url_o")
            .build();

    private FlickrApiConstants() {

    }

}
