package com.github.dimanolog.flickr.db;

public class FlickrDbSchema {
    public static final class PhotoTable {
        public static final String NAME = "photos";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String TITLE = "title";
            public static final String UPLOAD_DATE = "upload_date";
            public static final String OWNER = "owner";
            public static final String URL = "url";
        }
    }
}
