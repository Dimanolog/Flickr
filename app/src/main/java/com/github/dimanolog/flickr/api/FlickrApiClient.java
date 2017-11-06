package com.github.dimanolog.flickr.api;

import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.http.interfaces.IHttpClient;
import com.github.dimanolog.flickr.model.flickr.IPhoto;
import com.github.dimanolog.flickr.parsers.photo.PhotoParserFactory;
import com.github.dimanolog.flickr.util.IOUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class FlickrApiClient implements IFlickrApiClient {
    private static final String GET_RECENT_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";

    private static final String METHOD = "method";
    private static final String PAGE = "page";

    private IHttpClient mHttpClient = new HttpClient();


    public List<IPhoto> getRecent(int page) {
        String url = ApiConstants.ENDPOINT.buildUpon()
                .appendQueryParameter(METHOD, GET_RECENT_METHOD)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .build()
                .toString();

        return run(url);
    }

    public List<IPhoto> searchPhotos(int page, String search) {
        String url = ApiConstants.ENDPOINT.buildUpon()
                .appendQueryParameter(METHOD, SEARCH_METHOD)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .build()
                .toString();

        return run(url);

    }

    private List<IPhoto> run(String pUrl){
        FlickrApiResponseListener listener = new FlickrApiResponseListener();
        mHttpClient.request(pUrl, listener);
        return listener.getPhotoList();
    }

    private static class FlickrApiResponseListener implements HttpClient.ResponseListener {
        private List<IPhoto> mPhotoList;

        @Override
        public void onResponse(InputStream inputStream) throws IOException, JSONException {
            String jsonString = IOUtils.toString(inputStream);
            mPhotoList = new PhotoParserFactory()
                    .getGsonParser()
                    .parseArray(jsonString);
        }

        public List<IPhoto> getPhotoList() {
            return mPhotoList;
        }
    }
}
