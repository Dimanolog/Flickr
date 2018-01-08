package com.github.dimanolog.flickr.api;

import android.support.annotation.WorkerThread;

import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.http.interfaces.IHttpClient;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.parsers.photo.PhotoParserFactory;
import com.github.dimanolog.flickr.util.IOUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FlickrApiPhotoClient implements IFlickrApiClient {
    private static final String GET_RECENT_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final String METHOD = "method";
    private static final String PAGE = "page";
    private static final String SEARCH_TEXT = "text";

    @WorkerThread
    @Override
    public IResponse<List<IPhoto>> getRecent(int page) {
        String url = FlickrApiConstants.ENDPOINT.buildUpon()
                .appendQueryParameter(METHOD, GET_RECENT_METHOD)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .build()
                .toString();

        return startRequest(url);
    }

    @WorkerThread
    @Override
    public IResponse<List<IPhoto>> searchPhotos(int page, String search) {
        String url = FlickrApiConstants.ENDPOINT.buildUpon()
                .appendQueryParameter(METHOD, SEARCH_METHOD)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .appendQueryParameter(SEARCH_TEXT, search)
                .build()
                .toString();

        return startRequest(url);
    }

    private IResponse<List<IPhoto>> startRequest(String pUrl) {
        FlickrApiResponseListener listener = new FlickrApiResponseListener();
        IHttpClient mHttpClient = new HttpClient();
        mHttpClient.request(pUrl, listener);

        return listener.getResult();
    }

    private static class FlickrApiResponseListener implements HttpClient.ResponseListener {
        private IResponse<List<IPhoto>> mResult;

        @Override
        public void onResponse(InputStream inputStream) throws IOException {
            String jsonString = IOUtils.toString(inputStream);
            List<IPhoto> photoList = parseJson(jsonString);
            mResult = new Response<>(photoList);
        }

        @Override
        public void onError(Throwable pThrowable) {
            mResult = new Response<>(pThrowable);
        }

        IResponse<List<IPhoto>> getResult() {
            return mResult;
        }

        private List<IPhoto> parseJson(String pJsonString) {
            try {
                return new PhotoParserFactory()
                        .getGsonParser()
                        .parseArray(pJsonString);
            } catch (JSONException pE) {
                throw new RuntimeException(pE);
            }
        }
    }
}
