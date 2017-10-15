package com.github.dimanolog.flickr.http;

import com.github.dimanolog.flickr.http.interfaces.IHttpClient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public class HttpClient implements IHttpClient {

    public interface ResponseListener {
        void onResponse(InputStream inputStream);
    }

    @Override
    public void request(String url, ResponseListener listener) {
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            InputStream is = con.getInputStream();
            listener.onResponse(is);
            con.disconnect();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

