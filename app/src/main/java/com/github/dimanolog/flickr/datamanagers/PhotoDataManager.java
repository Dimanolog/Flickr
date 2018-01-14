package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiPhotoClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.dataservice.PhotoDataService;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.threading.RequestExecutor;

import java.util.List;


public class PhotoDataManager {
    private static PhotoDataManager sInstance;

    private Context mContext;
    private IFlickrApiClient mIFlickrApiClient = new FlickrApiPhotoClient();
    private IManagerCallback<ICustomCursorWrapper<IPhoto>> mIManagerCallback;
    private PhotoDataService mPhotoDataService;


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
        mPhotoDataService = new PhotoDataService(pContext);
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
                    mSearchPhotosFromDb = mPhotoDataService.addSearchQueryResultToDb(mResponse.getResult(), pQuery);
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

        RequestExecutor.executeRequestSerial(request);
    }

    public void getRecent(final int pPage) {
        RequestExecutor.executeRequestSerial(new GetRecentRequest(pPage));
    }

    public void registerCallback(@NonNull IManagerCallback<ICustomCursorWrapper<IPhoto>> pCallback) {
        mIManagerCallback = pCallback;
    }

    public void unRegisterCallback() {
        mIManagerCallback = null;
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
                mAllPhotosFromDb = mPhotoDataService.getRecent();
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
