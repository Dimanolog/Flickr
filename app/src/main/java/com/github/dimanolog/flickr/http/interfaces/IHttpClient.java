package com.github.dimanolog.flickr.http.interfaces;

import com.github.dimanolog.flickr.http.HttpClient;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public interface IHttpClient {

   public void request(String url, HttpClient.ResponseListener listener);



}
