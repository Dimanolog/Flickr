package com.github.dimanolog.flickr.api;

import android.net.Uri;

import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.parsers.responsestatus.ResponseStatusParserFactory;
import com.github.dimanolog.flickr.util.DateTimeUtil;
import com.github.dimanolog.flickr.util.IOUtils;
import com.github.dimanolog.flickr.util.SecureUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.github.dimanolog.flickr.api.FlickrApiConstants.FLICKR_API_URL;

public class FlickrApiAuthorizationClient {
    private static final String TAG = FlickrApiAuthorizationClient.class.getSimpleName();

    static final String OAUTH_BASE_URL = "https://www.flickr.com/services/oauth";
    static final String OAUTH_CALLBACK = "oauth_callback";
    static final String OAUTH_TOKEN = "oauth_token";
    static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    static final String OAUTH_NONCE = "oauth_nonce";
    static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    static final String OAUTH_VERSION = "oauth_version";
    static final String SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    static final String VERSION_VALUE = "1.0";
    static final String OAUTH_SIGNATURE_PARAM = "oauth_signature";


    public Response<String> getAccesToken(UserSession pUserSession) {
        Uri requestAccessUri = Uri.parse(OAUTH_BASE_URL)
                .buildUpon()
                .appendPath("access_token")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY)
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

    public IResponse<IResponseStatus> checkToken(String pOAuthToken) {
        Uri checkTokenUri = FLICKR_API_URL
                .buildUpon()
                .appendQueryParameter(OAUTH_TOKEN, pOAuthToken)
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
                .appendPath("request_token")
                .appendQueryParameter(OAUTH_CALLBACK, "flickr://callback")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .build();

        String oAuthSignature = SecureUtil.getAuthSignature(requestTokenUri, null);

        return requestTokenUri
                .buildUpon()
                .appendQueryParameter("oauth_signature", oAuthSignature)
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
                .appendPath("authorize")
                .appendQueryParameter(OAUTH_TOKEN, pOAuthToken)
                .build();
    }
}
