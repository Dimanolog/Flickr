package com.github.dimanolog.flickr.dataservice;

import android.content.Context;

import com.github.dimanolog.flickr.db.dao.PhotoDAO;
import com.github.dimanolog.flickr.db.dao.SearchQueryDAO;
import com.github.dimanolog.flickr.db.dao.SearchQueryToPhotoDAO;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.schema.FlickrDbSchema.PhotoTable;
import com.github.dimanolog.flickr.model.flickr.SearchQuery;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.model.flickr.interfaces.ISearchQuery;

import java.util.List;

/**
 * Created by Dimanolog on 02.01.2018.
 */

public class PhotoDataService {

    private PhotoDAO mPhotoDAO;
    private SearchQueryDAO mSearchQueryDAO;
    private SearchQueryToPhotoDAO mSearchQueryToPhotoDAO;

    public PhotoDataService(Context pContext) {
        mPhotoDAO = new PhotoDAO(pContext);
        mSearchQueryDAO = new SearchQueryDAO(pContext);
        mSearchQueryToPhotoDAO = new SearchQueryToPhotoDAO(pContext);
    }

    public ICustomCursorWrapper<IPhoto> addSearchQueryResultToDb(List<IPhoto> pPhotoList, String pSearchQuery) {
        mPhotoDAO.bulkInsert(pPhotoList);
        Long id = getQueryId(pSearchQuery);
        if (id != null) {
            mSearchQueryToPhotoDAO.addConnection(pPhotoList, id);
        }
        return getSearchQueryResult(id);
    }

    public ICustomCursorWrapper<IPhoto> addRecent(List<IPhoto> pPhotoList) {
        mPhotoDAO.bulkInsert(pPhotoList);
        return getRecent();
    }

    public ICustomCursorWrapper<IPhoto> getRecent() {
        return mPhotoDAO.getAll(PhotoTable.Cols.ID, PhotoDAO.DESC);
    }

    public ICustomCursorWrapper<IPhoto> getSearchQueryResult(Long pSearchQueryId) {
        return mPhotoDAO.getPhotosBySearchId(pSearchQueryId, PhotoTable.Cols.ID, PhotoDAO.DESC);
    }

    private Long getQueryId(String pSearchQuery) {
        ICustomCursorWrapper<ISearchQuery> searchQueryCursor = mSearchQueryDAO.getByQuery(pSearchQuery);
        Long id;
        if (searchQueryCursor.getCount() == 0) {
            ISearchQuery searchQuery = new SearchQuery();
            searchQuery.setQuery(pSearchQuery);
            id = mSearchQueryDAO.insert(searchQuery);
        } else {
            searchQueryCursor.moveToFirst();
            id = searchQueryCursor.get().getId();
        }

        return id;
    }
}
