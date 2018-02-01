package com.github.dimanolog.flickr.datamanagers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.interfaces.IFlickrApiPhotoClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.dataservice.PhotoDataService;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.depencyInjection.component.ApiComponent;
import com.github.dimanolog.flickr.depencyInjection.component.DaggerApiComponent;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;
import com.github.dimanolog.flickr.threading.RequestExecutor;

import java.util.List;

import javax.inject.Inject;


public class PhotoDataManager {
    private static PhotoDataManager sInstance;

    private Context mContext;
    @Inject
    IFlickrApiPhotoClient mIFlickrApiPhotoClient;
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
        ApiComponent component = DaggerApiComponent.builder().build();
        component.inject(this);
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
                mResponse = mIFlickrApiPhotoClient.searchPhotos(pPage, pQuery);
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
            mResponse = mIFlickrApiPhotoClient.getRecent(mPage);
            if (!mResponse.isError()) {
                mAllPhotosFromDb = mPhotoDataService.addRecentAndGetCursor(mResponse.getResult());
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
