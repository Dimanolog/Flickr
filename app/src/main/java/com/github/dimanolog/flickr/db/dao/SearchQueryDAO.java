package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.SearchQueryCursorWrapper;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema;
import com.github.dimanolog.flickr.model.flickr.SearchQuery;
import com.github.dimanolog.flickr.model.flickr.interfaces.ISearchQuery;

/**
 * Created by Dimanolog on 01.01.2018.
 */

public class SearchQueryDAO extends AbstractDAO<ISearchQuery> {

    public SearchQueryDAO(Context pContext) {
        super(pContext, SearchQuery.class);
    }

    @Override
    protected ICustomCursorWrapper<ISearchQuery> wrapCursor(Cursor pCursor) {
        return new SearchQueryCursorWrapper(pCursor);
    }

    @Override
    protected ContentValues entityToContentValues(ISearchQuery pEntity) {
        ContentValues values = new ContentValues();
        values.put(FlickrDbSchema.QueryTable.Cols.ID, pEntity.getId());
        values.put(FlickrDbSchema.QueryTable.Cols.QUERY, pEntity.getQuery());

        return values;
    }

    public ICustomCursorWrapper<ISearchQuery> getByQuery(String pQuery){
           return super.query(FlickrDbSchema.QueryTable.Cols.QUERY + " = ?", new String[]{pQuery});
    }
}
