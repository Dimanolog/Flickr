package com.github.dimanolog.flickr.api;

import android.net.Uri;

import com.github.dimanolog.flickr.datamanagers.UserSession;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.util.DateTimeUtil;
import com.github.dimanolog.flickr.util.IOUtils;
import com.github.dimanolog.flickr.util.SecureUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.github.dimanolog.flickr.api.FlickrApiConstants.FLICKR_BASE_URL;


public class FlickrApiAuthorizationClient {
    private static final String AUTH_BASE_URL = "https://www.flickr.com/services/oauth";
    private static final String OAUTH_CALLBACK = "oauth_callback";
    private static final String OAUTH_TOKEN = "oauth_token";
    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String VERSION_VALUE = "1.0";
    private static final String OAUTH_SIGNATURE_PARAM = "oauth_signature";



    public static String getAccesToken(UserSession pUserSession){
        Uri requestAccessUri = Uri.parse(AUTH_BASE_URL)
                .buildUpon()
                .appendPath("access_token")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter("oauth_token", pUserSession.getOAuthToken())
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .appendQueryParameter("oauth_verifier", pUserSession.getOAuthVerifier())
                .build();

        String oAuthSignature = getAuthSignature(requestAccessUri);
        requestAccessUri
                .buildUpon()
                .appendQueryParameter(OAUTH_SIGNATURE_PARAM, oAuthSignature)
                .build();
        try {
            InputStream inputStream = new HttpClient().request(requestToken().toString());
            String result = IOUtils.toString(inputStream);

            return new Response<>(result);
        } catch (IOException pE) {
            pE.printStackTrace();

            return new Response<>(pE);
        }


    }

    public static Uri requestToken() {
        Uri requestTokenUri = Uri.parse(AUTH_BASE_URL)
                .buildUpon()
                .appendPath("request_token")
                .appendQueryParameter(OAUTH_CALLBACK, "flickr://callback")
                .appendQueryParameter(OAUTH_CONSUMER_KEY, FlickrApiConstants.API_KEY)
                .appendQueryParameter(OAUTH_NONCE, UUID.randomUUID().toString())
                .appendQueryParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD_VALUE)
                .appendQueryParameter(OAUTH_TIMESTAMP, DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter(OAUTH_VERSION, VERSION_VALUE)
                .build();

        String oAuthSignature = getAuthSignature(requestTokenUri);

        return requestTokenUri
                .buildUpon()
                .appendQueryParameter("oauth_signature", oAuthSignature)
                .build();
    }



    public static Response<String> getRequestToken() {

        try {
            InputStream inputStream = new HttpClient().request(requestToken().toString());
            String result = IOUtils.toString(inputStream);

            return new Response<>(result);
        } catch (IOException pE) {
            pE.printStackTrace();

            return new Response<>(pE);
        }
    }

    public static Uri getUserAuthorizationUri(String pOAuthToken) {

        return Uri.parse(AUTH_BASE_URL)
                .buildUpon()
                .appendPath("authorize")
                .appendQueryParameter(OAUTH_TOKEN, pOAuthToken)
                .build();
    }

    private static String getAuthSignature(Uri pUri) {
        String parameters = pUri.getEncodedQuery();

        parameters = Uri.encode(parameters);
        String path = Uri.encode(pUri.getPath());
        String base = "GET&" + Uri.encode(FLICKR_BASE_URL) + path + "&" + parameters;
        String key = FlickrApiConstants.SECRET_KEY + "&";

        return SecureUtil.encryptByHmacSHA1(base, key);
    }
}
