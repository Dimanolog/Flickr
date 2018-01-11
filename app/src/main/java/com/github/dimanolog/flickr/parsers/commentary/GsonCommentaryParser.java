/*
 * Copyright (c) 2018 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.parsers.commentary;

import com.github.dimanolog.flickr.model.flickr.Commentary;
import com.github.dimanolog.flickr.model.flickr.interfaces.ICommentary;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.List;

public class GsonCommentaryParser implements IParser<ICommentary> {

    private final Gson mGson;
/*
    { "comments": { "photo_id": "109722179",
            "comment": [
        { "id": "6065-109722179-72057594077818641", "author": "35468159852@N01", "author_is_deleted": 0, "authorname": "Rev Dan Catt", "iconserver": "2865", "iconfarm": 3, "datecreate": "1141841470", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72057594077818641", "path_alias": "revdancatt", "realname": "Daniel Catt", "_content": "Umm, I'm not sure, can I get back to you on that one?" },
        { "id": "6065-109722179-72057594078656978", "author": "48692997@N00", "author_is_deleted": 0, "authorname": "thejacksons", "iconserver": "3717", "iconfarm": 4, "datecreate": "1141944043", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72057594078656978", "path_alias": "thejacksons", "realname": "Julie Jackson", "_content": "Yes!" },
        { "id": "6065-109722179-72057594078678184", "author": "50502690@N00", "author_is_deleted": 0, "authorname": "Joseph Robertson", "iconserver": "696", "iconfarm": 1, "datecreate": "1141946622", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72057594078678184", "path_alias": "josephrobertson", "realname": "Joseph Robertson", "_content": "Actually, I'd prefer a barrel of fun." },
        { "id": "6065-109722179-72157604275254302", "author": "51035734193@N01", "author_is_deleted": 0, "authorname": "kellan", "iconserver": 54, "iconfarm": 1, "datecreate": "1206664178", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157604275254302", "path_alias": "kellan", "realname": "Kellan", "_content": "<a href=\"http:\/\/www.flickr.com\/groups\/21951352@N00\/\">FlickrHQ<\/a>?" },
        { "id": "6065-109722179-72157605727409485", "author": "84712921@N00", "author_is_deleted": 0, "authorname": "phrawggmak", "iconserver": 0, "iconfarm": 0, "datecreate": "1214005968", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157605727409485", "path_alias": "phrawggmak", "realname": "", "_content": "OSM! FUn now comes in boxes!" },
        { "id": "6065-109722179-72157635339372249", "author": "100182176@N07", "author_is_deleted": 0, "authorname": "Ravi Somayaji", "iconserver": "7435", "iconfarm": 8, "datecreate": "1378121054", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157635339372249", "path_alias": "", "realname": "Ravi Somayaji", "_content": "[http:\/\/www.flickr.com\/photos\/phrawggmak] Fun fun" },
        { "id": "6065-109722179-72157649114571211", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415077792", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157649114571211", "path_alias": "", "realname": "Shrutika Rao", "_content": "hi test" },
        { "id": "6065-109722179-72157649116283242", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415082012", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157649116283242", "path_alias": "", "realname": "Shrutika Rao", "_content": "hi test3" },
        { "id": "6065-109722179-72157648715994660", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415082425", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157648715994660", "path_alias": "", "realname": "Shrutika Rao", "_content": "hi test4" },
        { "id": "6065-109722179-72157649061863296", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415083086", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157649061863296", "path_alias": "", "realname": "Shrutika Rao", "_content": "hi test 5" },
        { "id": "6065-109722179-72157646793580894", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415083151", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157646793580894", "path_alias": "", "realname": "Shrutika Rao", "_content": "hi test6" },
        { "id": "6065-109722179-72157646793643704", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415083534", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157646793643704", "path_alias": "", "realname": "Shrutika Rao", "_content": "hi test7" },
        { "id": "6065-109722179-72157649128380395", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415083784", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157649128380395", "path_alias": "", "realname": "Shrutika Rao", "_content": "test8" },
        { "id": "6065-109722179-72157648708658597", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415084844", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157648708658597", "path_alias": "", "realname": "Shrutika Rao", "_content": "test9" },
        { "id": "6065-109722179-72157648716378220", "author": "128985299@N03", "author_is_deleted": 0, "authorname": "shrutikarao", "iconserver": 0, "iconfarm": 0, "datecreate": "1415084963", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157648716378220", "path_alias": "", "realname": "Shrutika Rao", "_content": "test10" },
        { "id": "6065-109722179-72157649130422055", "author": "35468159852@N01", "author_is_deleted": 0, "authorname": "Rev Dan Catt", "iconserver": "2865", "iconfarm": 3, "datecreate": "1415097112", "permalink": "https:\/\/www.flickr.com\/photos\/straup\/109722179\/#comment72157649130422055", "path_alias": "revdancatt", "realname": "Daniel Catt", "_content": "teeeeeeeeeeest" }
    ] }, "stat": "ok" }*/

    GsonCommentaryParser() {
        mGson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public ICommentary parseObject(String pJsonString) {
        return mGson.fromJson(pJsonString, Commentary.class);
    }

    @Override
    public List<ICommentary> parseArray(String pJsonString) {
        JsonParser parser = new JsonParser();
        JsonObject jsonBody = parser.parse(pJsonString).getAsJsonObject();
        JsonObject jsonObjComments = jsonBody.getAsJsonObject("comments");
        JsonArray jsonArrayComments = jsonObjComments.getAsJsonArray("comment");
        ICommentary[] commentaries = mGson.fromJson(jsonArrayComments, Commentary[].class);

        return Arrays.asList(commentaries);
    }
}
