package com.github.dimanolog.flickr.api;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;
import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.parsers.responsestatus.ResponseStatusParserFactory;
import com.github.dimanolog.flickr.util.DateTimeUtil;
import com.github.dimanolog.flickr.util.IOUtils;
import com.github.dimanolog.flickr.util.SecureUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.github.dimanolog.flickr.api.FlickrApiConstants.FLICKR_API_BASE_URL;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.FORMAT_PARAM;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.FORMAT_VALUE;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.METHOD_PARAM;
import static com.github.dimanolog.flickr.api.FlickrApiConstants.NOJSONCALLBACK;

public class FlickrApiAuthorizationClient {
    private static final String TAG = FlickrApiAuthorizationClient.class.getSimpleName();

    private static final String OAUTH_BASE_URL = "https://www.flickr.com/services/oauth";
    private static final String OAUTH_CALLBACK = "oauth_callback";
    private static final String FLICKR_CALLBACK = "flickr://callback";
    private static final String OAUTH_CHECK_TOKEN_METHOD = "flickr.auth.oauth.checkToken";
    private static final String REQUEST_TOKEN_PATH = "request_token";
    private static final String ACCESS_TOKEN_PATH = "access_token";
    private static final String AUTHORIZE_PATH = "authorize";

    static final String OAUTH_TOKEN = "oauth_token";
    static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    static final String OAUTH_NONCE = "oauth_nonce";
    static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    static final String OAUTH_VERSION = "oauth_version";
    static final String SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    static final String VERSION_VALUE = "1.0";
    static final String OAUTH_SIGNATURE_PARAM = "oauth_signature";



    @WorkerThread
    public Response<String> getAccesToken(@NonNull UserSession pUserSession) {
        Uri requestAccessUri = Uri.parse(OAUTH_BASE_URL)
                .buildUpon()
                .appendPath(ACCESS_TOKEN_PATH)
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY_VALUE)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_TOKEN, pUserSession.getOAuthToken())
                .appendQueryParameter("oauth_verifier", pUserSession.getOAuthVerifier())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .build();

        String oAuthSignature = SecureUtil.getAuthSignature(requestAccessUri, pUserSession.getOAuthTokenSecret().trim());

        requestAccessUri = requestAccessUri
                .buildUpon()
                .appendQueryParameter(OAUTH_SIGNATURE_PARAM, oAuthSignature)
                .build();
        try {
            InputStream inputStream = new HttpClient().request(requestAccessUri.toString());
            String result = IOUtils.toString(inputStream);

            return new Response<>(result);
        } catch (IOException pE) {
            pE.printStackTrace();

            return new Response<>(pE);
        }
    }
    @WorkerThread
    public IResponse<IResponseStatus> checkToken(@NonNull UserSession pUserSession ) {
        Uri checkTokenUri = Uri.parse(FLICKR_API_BASE_URL)
                .buildUpon()
                .appendQueryParameter(FORMAT_PARAM, FORMAT_VALUE)
                .appendQueryParameter(METHOD_PARAM, OAUTH_CHECK_TOKEN_METHOD)
                .appendQueryParameter(NOJSONCALLBACK, "1")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY_VALUE)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_TOKEN, pUserSession.getOAuthToken())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .build();

        String oAuthSignature = SecureUtil.getAuthSignature(checkTokenUri, pUserSession.getOAuthTokenSecret().trim());

        checkTokenUri = checkTokenUri
                .buildUpon()
                .appendQueryParameter(OAUTH_SIGNATURE_PARAM, oAuthSignature)
                .build();

        AbstractHttpJsonResponseListener<IResponseStatus> listener = new AbstractHttpJsonResponseListener<IResponseStatus>() {
            @Override
            protected void responseAction(InputStream pInputStream) throws IOException {
                String s = IOUtils.toString(pInputStream);
                IResponseStatus responseStatus;
                responseStatus = new ResponseStatusParserFactory()
                        .getGsonParser()
                        .parseObject(s);
                setResponce(responseStatus);
            }
        };

        new HttpClient().request(checkTokenUri.toString(), listener);

        return listener.getResponse();
    }

    public Uri requestToken() {
        Uri requestTokenUri = Uri.parse(OAUTH_BASE_URL)
                .buildUpon()
                .appendPath(REQUEST_TOKEN_PATH)
                .appendQueryParameter(OAUTH_CALLBACK, FLICKR_CALLBACK)
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY_VALUE)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .build();

        String oAuthSignature = SecureUtil.getAuthSignature(requestTokenUri, null);

        return requestTokenUri
                .buildUpon()
                .appendQueryParameter(OAUTH_SIGNATURE_PARAM, oAuthSignature)
                .build();
    }


    public Response<String> getRequestToken() {

        try {
            InputStream inputStream = new HttpClient().request(requestToken().toString());
            String result = IOUtils.toString(inputStream);

            return new Response<>(result);
        } catch (IOException pE) {
            pE.printStackTrace();

            return new Response<>(pE);
        }
    }

    public Uri getUserAuthorizationUri(String pOAuthToken) {
        return Uri.parse(OAUTH_BASE_URL)
                .buildUpon()
                .appendPath(AUTHORIZE_PATH)
                .appendQueryParameter(OAUTH_TOKEN, pOAuthToken)
                .build();
    }
}
