package com.github.dimanolog.flickr.dataloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
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
        IRequest<ICustomCursorWrapper<IPhoto>> request = new IRequest<ICustomCursorWrapper<IPhoto>>() {
            @Override
            public ICustomCursorWrapper<IPhoto> runRequest() {
                List<IPhoto> photoList = mIFlickrApiClient.searchPhotos(pPage, query) ;
                addResultToDb(photoList);

                return getAllPhotosFromDb();
            }
        };

        startLoading(request);
    }

    public void getRecent(final int pPage) {
        IRequest<ICustomCursorWrapper<IPhoto>> request = new IRequest<ICustomCursorWrapper<IPhoto>>() {
            @Override
            public ICustomCursorWrapper<IPhoto> runRequest() {
                List<IPhoto> photoList = mIFlickrApiClient.getRecent(pPage);
                addResultToDb(photoList);

                return getAllPhotosFromDb();
            }
        };

        startLoading(request);
    }

    public void registerCallback(@NonNull IDataProviderCallback<ICustomCursorWrapper<IPhoto>> pCallback) {
        mIDataProviderCallback = pCallback;
    }

    public void unRegisterCallback(){
        mIDataProviderCallback=null;
    }

    private void startLoading(@NonNull IRequest<ICustomCursorWrapper<IPhoto>> pRequest) {
        RequestTask requestTask = new RequestTask(mIDataProviderCallback);
        requestTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, pRequest);
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


    private static class RequestTask extends AsyncTask<IRequest<ICustomCursorWrapper<IPhoto>>, Void, ICustomCursorWrapper<IPhoto>> {

        private IDataProviderCallback<ICustomCursorWrapper<IPhoto>> mIDataProviderCallback;

        RequestTask(IDataProviderCallback<ICustomCursorWrapper<IPhoto>> pIDataProviderCallback) {
            mIDataProviderCallback = pIDataProviderCallback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mIDataProviderCallback != null) {
                mIDataProviderCallback.onStartLoading();
            }
        }

        @Override
        protected ICustomCursorWrapper<IPhoto> doInBackground(IRequest<ICustomCursorWrapper<IPhoto>>[] pRequests) {
            return pRequests != null ? pRequests[0].runRequest() : null;
        }

        @Override
        protected void onPostExecute(ICustomCursorWrapper<IPhoto> pResult) {
            super.onPostExecute(pResult);
            if(mIDataProviderCallback!=null){
                mIDataProviderCallback.onSuccessResult(pResult);
            }
        }
    }
}
