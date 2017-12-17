package com.github.dimanolog.flickr.http.interfaces;

import com.github.dimanolog.flickr.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public interface IHttpClient {

    void request(String url, HttpClient.ResponseListener listener);

    InputStream request(String pUrl) throws IOException;

}
