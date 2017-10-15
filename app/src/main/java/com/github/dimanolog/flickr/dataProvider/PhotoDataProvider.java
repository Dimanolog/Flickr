package com.github.dimanolog.flickr.dataProvider;

import android.net.Uri;

import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.http.interfaces.IHttpClient;
import com.github.dimanolog.flickr.model.Photo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimanolog on 15.10.2017.
 */

public class PhotoDataProvider {

    private static final String TAG = PhotoDataProvider.class.getSimpleName();
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final String API_KEY = "47e8d1e158478d2fd8c02a85d0350293";

    private static final Uri ENDPOINT = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

    List<Photo> mPhotoList=new ArrayList<>();

    private void getPhotosHttp(){
        IHttpClient httpClient=new HttpClient();
        httpClient.request(ENDPOINT.toString(), new HttpClient.ResponseListener() {
            @Override
            public void onResponse(InputStream inputStream) {
                
            }
        });

    }


}
