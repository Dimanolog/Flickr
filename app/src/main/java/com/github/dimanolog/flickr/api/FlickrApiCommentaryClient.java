package com.github.dimanolog.flickr.api;


import android.net.Uri;
import android.support.annotation.WorkerThread;

import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.datamanagers.UserSession;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.parsers.commentary.CommenataryParserFactory;
import com.github.dimanolog.flickr.util.DateTimeUtil;
import com.github.dimanolog.flickr.util.IOUtils;
import com.github.dimanolog.flickr.util.SecureUtil;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_CONSUMER_KEY;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_NONCE;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_SIGNATURE_METHOD;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_SIGNATURE_PARAM;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_TIMESTAMP;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_TOKEN;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.OAUTH_VERSION;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.SIGNATURE_METHOD_VALUE;
import static com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient.VERSION_VALUE;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.FLICKR_API_BASE_URL;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.FLICKR_API_URL;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.METHOD_PARAM;


public class FlickrApiCommentaryClient {

    /* https://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=90269d7b8d7762c26bb29b2860d08ea9
          // &photo_id=109722179&format=json&nojsoncallback=1&api_sig=3e547cfa09162d84cd53d83c0e245930*/
    @WorkerThread
    public IResponse<List<ICommentary>> getListOfCommentByPhoto(IPhoto pPhoto) {
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

    // https://api.flickr.com/services/rest/?method=flickr.photos.comments.addComment
    // &api_key=90269d7b8d7762c26bb29b2860d08ea9&photo_id=24761471527
    // &comment_text=123123&format=json&nojsoncallback=1
    // &api_sig=27447e0a2a2bcdd1b57260087891daa4

    @WorkerThread
    public IResponse<ResponseStatus> addComment(Long pPhotoId, String pCommentText, UserSession pUserSession) {
        Uri addcommenatUri = Uri.parse(FLICKR_API_BASE_URL)
                .buildUpon()
                .appendQueryParameter("comment_text", pCommentText)
                .appendQueryParameter(METHOD_PARAM, "flickr.photos.comments.addComment")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_TOKEN, pUserSession.getOAuthToken())
                .appendQueryParameter("oauth_verifier", pUserSession.getOAuthVerifier())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .appendQueryParameter("photo_id", String.valueOf(pPhotoId))

                .build();

        String oAuthSignature = SecureUtil.getAuthSignature(addcommenatUri, pUserSession.getOAuthTokenSecret().trim());

        addcommenatUri = addcommenatUri
                .buildUpon()
                .appendQueryParameter(OAUTH_SIGNATURE_PARAM, oAuthSignature)
                .build();


        AbstractHttpJsonResponseListener<ResponseStatus> listener = new AbstractHttpJsonResponseListener<ResponseStatus>() {
            @Override
            protected void responseAction(InputStream pInputStream) throws IOException {
                String s = IOUtils.toString(pInputStream);
            }
        };
        new HttpClient().request(addcommenatUri.toString(), listener);

        return listener.getResponse();
    }
}
