package com.github.dimanolog.flickr.model.flickr;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.interfaces.ISearchQuery;

@Table(FlickrDbSchema.QueryTable.NAME)
public class SearchQuery implements ISearchQuery {
    @Identity(value = FlickrDbSchema.QueryTable.Cols.ID, autoincrement = true)
    Long mId;
    @Column(FlickrDbSchema.QueryTable.Cols.QUERY)
    String mQuery;

    @Override
    public Long getId() {
        return mId;
    }

    @Override
    public void setId(Long pId) {
        mId = pId;
    }

    @Override
    public String getQuery() {
        return mQuery;
    }

    @Override
    public void setQuery(String pQuery) {
        mQuery = pQuery;
    }
}
