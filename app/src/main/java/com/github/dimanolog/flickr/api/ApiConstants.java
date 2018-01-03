package com.github.dimanolog.flickr.api;

import android.net.Uri;


class ApiConstants {
    private static final String API_KEY = "47e8d1e158478d2fd8c02a85d0350293";
    static final Uri ENDPOINT = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "date_upload,url_z,url_o")
            .build();

    private  ApiConstants(){}

}
