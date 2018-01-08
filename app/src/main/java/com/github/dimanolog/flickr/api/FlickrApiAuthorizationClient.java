package com.github.dimanolog.flickr.api;

import android.net.Uri;

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

    public static Uri requestToken() {
        Uri requestTokenUri = Uri.parse(AUTH_BASE_URL)
                .buildUpon()
                .appendPath("request_token")
                .appendQueryParameter(OAUTH_CALLBACK, "flickr://callback")
                .appendQueryParameter("oauth_consumer_key", FlickrApiConstants.API_KEY)
                .appendQueryParameter("oauth_nonce", UUID.randomUUID().toString())
                .appendQueryParameter("oauth_signature_method", "HMAC-SHA1")
                .appendQueryParameter("oauth_timestamp", DateTimeUtil.getCurrentTimeStampString())
                .appendQueryParameter("oauth_version", "1.0")
                .build();

        String oAuthSignature = getAuthSignature(requestTokenUri);

        return requestTokenUri
                .buildUpon()
                .appendQueryParameter("oauth_signature", oAuthSignature)
                .build();
    }

    public static void getRequestToken() {
        final String s;
        try {
            InputStream inputStream = new HttpClient().request(requestToken().toString());
            String response = IOUtils.toString(inputStream);
            Uri parse = Uri.parse(response);
            String oAuthToken = parse.getQueryParameter("oauth_token");
            String oAuthTokenSecret = parse.getQueryParameter("oauth_token_secret");

        } catch (IOException pE) {
            pE.printStackTrace();
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
