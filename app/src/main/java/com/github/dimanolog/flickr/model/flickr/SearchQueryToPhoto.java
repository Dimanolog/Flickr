package com.github.dimanolog.flickr.model.flickr;

import com.github.dimanolog.flickr.db.annotations.Column;
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
    @ForeignKey(table = FlickrDbSchema.PhotoTable.NAME, column = FlickrDbSchema.PhotoTable.Cols.ID )
    @Column(FlickrDbSchema.SearchQueryToPhoto.Cols.photoId)
    private Long mPhotoId;
    @ForeignKey(table = FlickrDbSchema.QueryTable.NAME, column = FlickrDbSchema.QueryTable.Cols.ID)
    @Column(FlickrDbSchema.SearchQueryToPhoto.Cols.searchQueryId)
    private Long mSearchQueryId;
}
