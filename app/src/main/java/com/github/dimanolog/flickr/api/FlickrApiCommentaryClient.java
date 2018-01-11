package com.github.dimanolog.flickr.api;


import android.net.Uri;

import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.parsers.commentary.CommenataryParserFactory;
import com.github.dimanolog.flickr.util.IOUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.github.dimanolog.flickr.api.FlickrApiConstants.FLICKR_API_URL;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.METHOD_PARAM;

public class FlickrApiCommentaryClient {


    /* https://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=90269d7b8d7762c26bb29b2860d08ea9
     // &photo_id=109722179&format=json&nojsoncallback=1&api_sig=3e547cfa09162d84cd53d83c0e245930*/
    public IResponse<List<ICommentary>> getListOfCommentByPhoto(IPhoto pPhoto){
        final Uri commenatsUri = FLICKR_API_URL
                .buildUpon()
                .appendQueryParameter(METHOD_PARAM, "flickr.photos.comments.getList")
                .appendQueryParameter("photo_id", String.valueOf(pPhoto.getId()))
                .build();

        AbstractHttpJsonResponseListener<List<ICommentary>> listener = new AbstractHttpJsonResponseListener<List<ICommentary>>() {
            @Override
            protected void responseAction(InputStream pInputStream) throws IOException {
                String jsonString = IOUtils.toString(pInputStream);
                try {
                    List<ICommentary> commentaries = new CommenataryParserFactory().getGsonParser().parseArray(jsonString);
                    setResponce(commentaries);
                } catch (JSONException pE) {
                    throw new RuntimeException(pE);
                }

            }
        };
        new HttpClient().request(commenatsUri.toString(), listener);

        return listener.getResponse();

    }



}
