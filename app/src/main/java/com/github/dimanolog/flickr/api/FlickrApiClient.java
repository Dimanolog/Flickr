package com.github.dimanolog.flickr.api;

import android.support.annotation.WorkerThread;

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

    @WorkerThread
    public List<IPhoto> getRecent(int page) {
        String url = ApiConstants.ENDPOINT.buildUpon()
                .appendQueryParameter(METHOD, GET_RECENT_METHOD)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .build()
                .toString();
        String result = startRequest(url);

        return parseJson(result);
    }
    @WorkerThread
    public List<IPhoto> searchPhotos(int page, String search) {
        String url = ApiConstants.ENDPOINT.buildUpon()
                .appendQueryParameter(METHOD, SEARCH_METHOD)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .appendQueryParameter("text", search)
                .build()
                .toString();
        String result = startRequest(url);
        return parseJson(result);
    }

    private String startRequest(String pUrl) {
        FlickrApiResponseListener listener = new FlickrApiResponseListener();
        mHttpClient.request(pUrl, listener);
        return listener.getResultStr();
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

    private static class FlickrApiResponseListener implements HttpClient.ResponseListener {
        private String mResultStr;
        private Throwable mThrowable;
        private boolean isError;

        @Override
        public void onResponse(InputStream inputStream) throws IOException {
            mResultStr = IOUtils.toString(inputStream);
        }

        @Override
        public void onError(Throwable t) {
            isError = true;
            mThrowable = t;
        }

        String getResultStr() {
            return mResultStr;
        }

        public Throwable getThrowable() {
            return mThrowable;
        }
    }
}
