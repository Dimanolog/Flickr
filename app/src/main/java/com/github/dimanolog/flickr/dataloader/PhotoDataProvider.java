package com.github.dimanolog.flickr.dataloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IResponse;
import com.github.dimanolog.flickr.db.dao.ICustomCursorWrapper;
import com.github.dimanolog.flickr.db.dao.PhotoDAO;
import com.github.dimanolog.flickr.model.flickr.IPhoto;

import java.util.ArrayList;
import java.util.List;


public class PhotoDataProvider {
    private static PhotoDataProvider sInstance;

    private Context mContext;
    private IFlickrApiClient mIFlickrApiClient = new FlickrApiClient();
    private IDataProviderCallback<ICustomCursorWrapper<IPhoto>> mIDataProviderCallback;
    private PhotoDAO mPhotoDAO;
    private List<IPhoto> mIPhotoList = new ArrayList<>();


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
    }

    public void searchPhotos(final int pPage, final String query) {
        IRequest request = new IRequest() {
            private IResponse<List<IPhoto>> mResponse;
            private ICustomCursorWrapper<IPhoto> mAllPhotosFromDb;

            @Override
            public void onPreRequest() {
                if (mIDataProviderCallback != null) {
                    mIDataProviderCallback.onStartLoading();
                }
            }

            @Override
            public void runRequest() {
                mResponse = mIFlickrApiClient.searchPhotos(pPage,query);
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

    public List<IPhoto> getResult() {
        return mIPhotoList;
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

    private  class GetRecentRequest implements IRequest {

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
