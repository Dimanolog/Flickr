package com.github.dimanolog.flickr.backend;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.github.dimanolog.flickr.BuildConfig;
import com.github.dimanolog.flickr.backend.model.Version;
import com.github.dimanolog.flickr.http.HttpClient;
import com.github.dimanolog.flickr.util.IOUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class FlickAppBackendClient {

    private static final Uri BASE_URL_BACKEND = Uri.parse(BuildConfig.BASE_CHECK_VERSION_URL);
    private static final String CHECK_VERSION = "checkVersion";

    public interface BackEndCallback {
        void onResult();
    }

    private Version mVersion;

    public Version getVersion() {
        return mVersion;
    }

    public void checkVersion(final BackEndCallback callback) {
        final String url = BASE_URL_BACKEND.buildUpon()
                .appendPath(CHECK_VERSION)
                .build()
                .toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpClient().request(url, new HttpClient.ResponseListener() {
                    @Override
                    public void onResponse(InputStream pInputStream) throws IOException {
                        mVersion = new Gson().fromJson(IOUtils.toString(pInputStream), Version.class);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResult();
                    }
                });
            }
        }).run();
    }
}
