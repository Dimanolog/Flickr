package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiPhotoClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.db.PhotoService;
import com.github.dimanolog.flickr.db.dao.PhotoDAO;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

import java.util.List;


public class PhotoDataManager {
    private static PhotoDataManager sInstance;

    private Context mContext;
    private IFlickrApiClient mIFlickrApiClient = new FlickrApiPhotoClient();
    private IManagerCallback<ICustomCursorWrapper<IPhoto>> mIManagerCallback;
    private PhotoDAO mPhotoDAO;
    private PhotoService mPhotoService;


    public static PhotoDataManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (PhotoDataManager.class) {
                if (sInstance == null) {
                    sInstance = new PhotoDataManager(context);
                }
            }
        }
        return sInstance;
    }

    private PhotoDataManager(@NonNull Context pContext) {
        mContext = pContext.getApplicationContext();
        mPhotoDAO = new PhotoDAO(pContext);
        mPhotoService = new PhotoService(pContext);
    }

    public void searchPhotos(final int pPage, final String pQuery) {
        IRequest request = new IRequest() {
            private IResponse<List<IPhoto>> mResponse;
            private ICustomCursorWrapper<IPhoto> mSearchPhotosFromDb;

            @Override
            public void onPreRequest() {
                if (mIManagerCallback != null) {
                    mIManagerCallback.onStartLoading();
                }
            }

            @Override
            public void runRequest() {
                mResponse = mIFlickrApiClient.searchPhotos(pPage, pQuery);
                if (!mResponse.isError()) {
                    Long id = mPhotoService.addSearchQueryResultToDb(mResponse.getResult(), pQuery);
                    mSearchPhotosFromDb = mPhotoService.getSearchQueryResult(id);
                }
            }

            @Override
            public void onPostRequest() {
                if (!mResponse.isError()) {
                    mIManagerCallback.onSuccessResult(mSearchPhotosFromDb);
                } else {
                    mIManagerCallback.onError(mResponse.getError());
                }
            }
        };

        startLoading(request);
    }

    public void getRecent(final int pPage) {
        startLoading(new GetRecentRequest(pPage));
    }

    public void registerCallback(@NonNull IManagerCallback<ICustomCursorWrapper<IPhoto>> pCallback) {
        mIManagerCallback = pCallback;
    }

    public void unRegisterCallback() {
        mIManagerCallback = null;
    }

    private void startLoading(@NonNull IRequest pRequest) {
        RequestTask requestTask = new RequestTask(pRequest);
        requestTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null);
    }


    private Integer addResultToDb(List<IPhoto> pPhotoList) {
        return mPhotoDAO.bulkInsert(pPhotoList);
    }

    private ICustomCursorWrapper<IPhoto> getAllPhotosFromDb() {
        return mPhotoDAO.getAll();
    }


    private class GetRecentRequest implements IRequest {

        private final int mPage;
        private IResponse<List<IPhoto>> mResponse;
        private ICustomCursorWrapper<IPhoto> mAllPhotosFromDb;

        GetRecentRequest(int pPage) {
            mPage = pPage;
        }

        @Override
        public void onPreRequest() {
            if (mIManagerCallback != null) {
                mIManagerCallback.onStartLoading();
            }
        }

        @Override
        public void runRequest() {
            mResponse = mIFlickrApiClient.getRecent(mPage);
            if (!mResponse.isError()) {
                addResultToDb(mResponse.getResult());
                mAllPhotosFromDb = getAllPhotosFromDb();
            }
        }

        @Override
        public void onPostRequest() {
            if (!mResponse.isError()) {
                mIManagerCallback.onSuccessResult(mAllPhotosFromDb);
            } else {
                mIManagerCallback.onError(mResponse.getError());
            }
        }
    }
}
