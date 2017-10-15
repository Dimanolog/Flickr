package com.github.dimanolog.flickr;

import android.net.Uri;
import android.util.Log;

import com.github.dimanolog.flickr.model.Photo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dimanolog on 11.11.2016.
 */
public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final String API_KEY = "47e8d1e158478d2fd8c02a85d0350293";

    private static final Uri ENDPOINT = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Photo> fetchRecentPhotos(Integer page) {
        String url = buildUrl(FETCH_RECENTS_METHOD, null,page );
        return downloadGalleryItems(url);
    }

    public List<Photo> searchPhotos(String query, Integer page) {
        String url = buildUrl(SEARCH_METHOD, query, page);
        return downloadGalleryItems(url);
    }

    private List<Photo> downloadGalleryItems(String url) {
        List<Photo> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            parseItems(items, jsonString);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parseObject JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    private String buildUrl(String method, String query, Integer page) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("method", method)
                .appendQueryParameter("page", String.valueOf(page));

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }

        return uriBuilder.build().toString();
    }


    private void parseItems(List<Photo> items, String jsonString) throws JSONException {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        JsonParser parser = new JsonParser();

        JsonObject jsonBody=parser.parse(jsonString).getAsJsonObject();

        JsonObject jsonObjPhotos=jsonBody.getAsJsonObject("photos");
        JsonArray jsonArrayPhoto=jsonObjPhotos.getAsJsonArray("photo");

        Type galleryListType=new TypeToken<Collection<Photo>>(){}
                .getType();

        List<Photo> galleryItemsList=gson.fromJson(jsonArrayPhoto,galleryListType);
        items.addAll(galleryItemsList);
        Log.d(TAG,"parseObject Json good");
    }
}
