package com.github.dimanolog.flickr.db;

import android.content.Context;

import com.github.dimanolog.flickr.db.dao.PhotoDAO;
import com.github.dimanolog.flickr.db.dao.SearchQueryDAO;
import com.github.dimanolog.flickr.db.dao.SearchQueryToPhotoDAO;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.SearchQuery;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.model.flickr.interfaces.ISearchQuery;

import java.util.List;

/**
 * Created by Dimanolog on 02.01.2018.
 */

public class PhotoService {

    private PhotoDAO mPhotoDAO;
    private SearchQueryDAO mSearchQueryDAO;
    private SearchQueryToPhotoDAO mSearchQueryToPhotoDAO;

    public PhotoService(Context pContext) {
        mPhotoDAO = new PhotoDAO(pContext);
        mSearchQueryDAO = new SearchQueryDAO(pContext);
        mSearchQueryToPhotoDAO = new SearchQueryToPhotoDAO(pContext);
    }

    public void addSearchQueryResultToDb(List<IPhoto> pPhotoList, String pSearchQuery) {
        mPhotoDAO.bulkInsert(pPhotoList);
        Long id = getQueryId(pSearchQuery);
        if (id != null) {
            mSearchQueryToPhotoDAO.addConnection(pPhotoList, id);
        }
    }

    public ICustomCursorWrapper<IPhoto> getSearchQueryResult(ISearchQuery pSearchQuery) {
        return mPhotoDAO.getPhotosBySearch(pSearchQuery);
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
