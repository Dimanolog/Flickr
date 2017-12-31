package com.github.dimanolog.flickr.db.schema;

public class FlickrDbSchema {
    public static final class PhotoTable {
        public static final String NAME = "photos";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String TITLE = "title";
            public static final String UPLOAD_DATE = "date_upload";
            public static final String OWNER = "owner";
            public static final String URL = "url";
        }
    }

    public static final class QueryTable {
        public static final String NAME = "search_queries";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String QUERY = "search_query";
        }
    }

    public static final class SearchQueryToPhoto {
        public static final String NAME = "searchquery_photo";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String searchQueryId = "search_query_id";
            public static final String photoId = "photo_id";
        }
    }
}

