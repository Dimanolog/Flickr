package com.github.dimanolog.flickr.model.flickr;

import com.github.dimanolog.flickr.db.annotations.ForeignKey;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;

/**
 * Created by Dimanolog on 31.12.2017.
 */
@Table(FlickrDbSchema.SearchQueryToPhoto.NAME)
public class SearchQueryToPhoto {
    @Identity( value = FlickrDbSchema.SearchQueryToPhoto.Cols.ID, autoincrement = true)
    private Long mId;
    @ForeignKey(name = FlickrDbSchema.SearchQueryToPhoto.Cols.photoId,
            table = FlickrDbSchema.PhotoTable.NAME, column = FlickrDbSchema.PhotoTable.Cols.ID )
    private Long mPhotoId;
    @ForeignKey(name = FlickrDbSchema.SearchQueryToPhoto.Cols.searchQueryId,
            table = FlickrDbSchema.QueryTable.NAME, column = FlickrDbSchema.QueryTable.Cols.ID)
    private Long mSearchQueryId;
}
