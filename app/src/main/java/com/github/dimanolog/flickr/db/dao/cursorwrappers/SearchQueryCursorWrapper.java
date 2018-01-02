package com.github.dimanolog.flickr.db.dao.cursorwrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.SearchQuery;
import com.github.dimanolog.flickr.model.flickr.interfaces.ISearchQuery;

/**
 * Created by Dimanolog on 01.01.2018.
 */

public class SearchQueryCursorWrapper extends CursorWrapper implements ICustomCursorWrapper<ISearchQuery> {

    public SearchQueryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    @Override
    public ISearchQuery get() {
        Long id = getLong(getColumnIndex(FlickrDbSchema.SearchQueryToPhotoTable.Cols.ID));
        String query = getString(getColumnIndex(FlickrDbSchema.QueryTable.Cols.QUERY));

        ISearchQuery searchQuery = new SearchQuery();
        searchQuery.setId(id);
        searchQuery.setQuery(query);

        return searchQuery;
    }
}
