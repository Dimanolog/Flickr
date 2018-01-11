package com.github.dimanolog.flickr.http;

import com.github.dimanolog.flickr.http.interfaces.IHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient implements IHttpClient {
    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 5000;

    public interface ResponseListener {
        void onResponse(InputStream pInputStream) throws IOException;

        void onError(Throwable t);
    }

    @Override
    public void request(String pUrl, ResponseListener pListener) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(pUrl).openConnection();
            tuneConnection(con);
            InputStream is = con.getInputStream();
            pListener.onResponse(is);
            con.disconnect();
        } catch (Throwable t) {
            t.printStackTrace();
            pListener.onError(t);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    @Override
    public InputStream request(String pUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) (new URL(pUrl)).openConnection();
        tuneConnection(connection);
        return connection.getInputStream();
    }

    private void tuneConnection(HttpURLConnection pConnection) {
        pConnection.setConnectTimeout(CONNECT_TIMEOUT);
        pConnection.setReadTimeout(READ_TIMEOUT);
    }
}

