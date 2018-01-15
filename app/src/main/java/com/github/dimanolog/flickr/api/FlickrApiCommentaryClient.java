package com.github.dimanolog.flickr.api;


import android.net.Uri;
import android.support.annotation.WorkerThread;

import com.github.dimanolog.flickr.api.interfaces.IFlickrApiCommentaryClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;
import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.parsers.commentary.CommenataryParserFactory;
import com.github.dimanolog.flickr.parsers.responsestatus.ResponseStatusParserFactory;
import com.github.dimanolog.flickr.util.DateTimeUtil;
import com.github.dimanolog.flickr.util.IOUtils;
import com.github.dimanolog.flickr.util.SecureUtil;

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
import static com.github.dimanolog.flickr.api.FlickrApiConstants.FORMAT_PARAM;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.FORMAT_VALUE;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.METHOD_PARAM;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.NOJSONCALLBACK;


public class FlickrApiCommentaryClient implements IFlickrApiCommentaryClient {

    private static final String COMMENTS_GET_LIST_METHOD = "flickr.photos.comments.getList";
    private static final String COMMENTS_ADD_COMMENT_METHOD = "flickr.photos.comments.addComment";
    private static final String COMMENT_TEXT_PARAM = "comment_text";
    private static final String PHOTO_ID_PARAM = "photo_id";

    @Override
    @WorkerThread
    public IResponse<List<ICommentary>> getListOfCommentByPhoto(IPhoto pPhoto) {
        final Uri commenatsUri = FLICKR_API_URL
                .buildUpon()
                .appendQueryParameter(METHOD_PARAM, COMMENTS_GET_LIST_METHOD)
                .appendQueryParameter("photo_id", String.valueOf(pPhoto.getId()))
                .build();

        AbstractHttpJsonResponseListener<List<ICommentary>> listener = new AbstractHttpJsonResponseListener<List<ICommentary>>() {
            @Override
            protected void responseAction(InputStream pInputStream) throws IOException {
                String jsonString = IOUtils.toString(pInputStream);
                List<ICommentary> commentaries = new CommenataryParserFactory().getGsonParser().parseArray(jsonString);
                setResponce(commentaries);
            }
        };
        new HttpClient().request(commenatsUri.toString(), listener);

        return listener.getResponse();

    }


    @Override
    @WorkerThread
    public IResponse<IResponseStatus> addComment(Long pPhotoId, String pCommentText, UserSession pUserSession) {
        Uri addcommenatUri = Uri.parse(FLICKR_API_BASE_URL)
                .buildUpon()
                .appendQueryParameter(COMMENT_TEXT_PARAM, pCommentText)
                .appendQueryParameter(FORMAT_PARAM, FORMAT_VALUE)
                .appendQueryParameter(METHOD_PARAM, COMMENTS_ADD_COMMENT_METHOD)
                .appendQueryParameter(NOJSONCALLBACK, "1")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY_VALUE)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_TOKEN, pUserSession.getOAuthToken())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .appendQueryParameter(PHOTO_ID_PARAM, String.valueOf(pPhotoId))

                .build();



        String oAuthSignature = SecureUtil.getAuthSignature(addcommenatUri, pUserSession.getOAuthTokenSecret().trim());

        addcommenatUri = addcommenatUri
                .buildUpon()
                .appendQueryParameter(OAUTH_SIGNATURE_PARAM, oAuthSignature)
                .build();


        AbstractHttpJsonResponseListener<IResponseStatus> listener = new AbstractHttpJsonResponseListener<IResponseStatus>() {
            @Override
            protected void responseAction(InputStream pInputStream) throws IOException {
                String jsonStr = IOUtils.toString(pInputStream);
                IResponseStatus responseStatus = new ResponseStatusParserFactory().getGsonParser().parseObject(jsonStr);
                setResponce(responseStatus);
            }
        };
        new HttpClient().request(addcommenatUri.toString(), listener);

        return listener.getResponse();
    }
}
