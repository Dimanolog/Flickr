package com.github.dimanolog.flickr.db.schema;

public class FlickrDbSchema {
    public static final class PhotoTable {
        public static final String NAME = "photo";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String TITLE = "title";
            public static final String UPLOAD_DATE = "date_upload";
            public static final String OWNER = "owner";
            public static final String SMALL_IMAGE_URL = "small_url";
            public static final String ORIGINAL_IMAGE_URL = "original_url";
        }
    }

    public static final class QueryTable {
        public static final String NAME = "search_query";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String QUERY = "search_query";
        }
    }

    public static final class SearchQueryToPhotoTable {
        public static final String NAME = "searchquery_photo";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String SEARCH_QUERY_ID = "search_query_id";
            public static final String PHOTO_ID = "photo_id";
        }
    }

    public static final class CommentaryTable {
        public static final String NAME = "comment";

        public static final class Cols {
            public static final String ID = "_ID";
            public static final String AUTHOR_NAME = "search_query_id";
            public static final String AUTHOR_IS_DELETED = "author_is_deleted";
            public static final String ICON_SERVER = "iconserver";
            public static final String ICON_FARM = "iconfarm";
            public static final String DATE_CREATE = "datecreate";
            public static final String PERMALINK = "permalink";
            public static final String PATH_ALIAS = "path_alias";
            public static final String REAL_NAME = "real_name";
            public static final String CONTENT = "content";
            public static final String PHOTO_ID = "photo_id";
        }
    }
}

