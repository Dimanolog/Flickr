package com.github.dimanolog.flickr.parsers.photo;

import com.github.dimanolog.flickr.model.Photo;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimanolog on 14.10.2017.
 */

public class JsonPhotoParser implements IParser<Photo> {

    private static final String TITLE = "title";
    private static final String ID = "id";
    private static final String OWNER = "owner";
    private static final String URL_S = "url_s";

    @Override
    public Photo parseObject(String jsonString) throws JSONException {
        return parsePhotoJsonObject(new JSONObject(jsonString));
    }


    @Override
    public List<Photo> parseArray(String jsonArrayStr) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        List<Photo> photoList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            photoList.add(parsePhotoJsonObject(jsonObject));
        }
        return photoList;
    }

    private Photo parsePhotoJsonObject(JSONObject jsonObject) {
        Photo photo = new Photo();
        photo.setCaption(jsonObject.optString(TITLE));
        photo.setId(jsonObject.optString(ID));
        photo.setOwner(jsonObject.optString(OWNER));
        photo.setUrl(jsonObject.optString(URL_S));

        return photo;
    }

}
