package com.github.dimanolog.flickr.dataloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.db.PhotoService;
import com.github.dimanolog.flickr.db.dao.PhotoDAO;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.model.flickr.interfaces.IPhoto;

import java.util.List;


public class PhotoDataProvider {
    private static PhotoDataProvider sInstance;

    private Context mContext;
    private IFlickrApiClient mIFlickrApiClient = new FlickrApiClient();
    private IDataProviderCallback<ICustomCursorWrapper<IPhoto>> mIDataProviderCallback;
    private PhotoDAO mPhotoDAO;
    private PhotoService mPhotoService;


    public static PhotoDataProvider getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (PhotoDataProvider.class) {
                if (sInstance == null) {
                    sInstance = new PhotoDataProvider(context);
                }
            }
        }
        return sInstance;
    }

    private PhotoDataProvider(@NonNull Context pContext) {
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
                if (mIDataProviderCallback != null) {
                    mIDataProviderCallback.onStartLoading();
                }
            }

            @Override
            public void runRequest() {
                mResponse = mIFlickrApiClient.searchPhotos(pPage, pQuery);
                if (!mResponse.isError()) {
                    mPhotoService.addSearchQueryResultToDb(mResponse.getResult(), pQuery);
                    mSearchPhotosFromDb = mPhotoService.getSearchQueryResult(pQuery)
                }
            }

            @Override
            public void onPostRequest() {
                if (!mResponse.isError()) {
                    mIDataProviderCallback.onSuccessResult(mSearchPhotosFromDb);
                } else {
                    mIDataProviderCallback.onError(mResponse.getError());
                }
            }
        };

        startLoading(request);
    }

    public void getRecent(final int pPage) {
        startLoading(new GetRecentRequest(pPage));
    }

    public void registerCallback(@NonNull IDataProviderCallback<ICustomCursorWrapper<IPhoto>> pCallback) {
        mIDataProviderCallback = pCallback;
    }

    public void unRegisterCallback() {
        mIDataProviderCallback = null;
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


    private static class RequestTask extends AsyncTask<Void, Void, Void> {
        private IRequest mIRequest;

        public RequestTask(IRequest pIRequest) {
            mIRequest = pIRequest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIRequest.onPreRequest();
        }

        @Override
        protected void onPostExecute(Void pVoid) {
            super.onPostExecute(pVoid);
            mIRequest.onPostRequest();
        }

        @Override
        protected Void doInBackground(Void... pVoids) {
            mIRequest.runRequest();
            return null;
        }
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
            if (mIDataProviderCallback != null) {
                mIDataProviderCallback.onStartLoading();
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
                mIDataProviderCallback.onSuccessResult(mAllPhotosFromDb);
            } else {
                mIDataProviderCallback.onError(mResponse.getError());
            }
        }
    }
}
