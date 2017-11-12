package com.github.dimanolog.flickr.dataloader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.model.flickr.IPhoto;

import java.util.ArrayList;
import java.util.List;


public class PhotoDataProvider {

    public static PhotoDataProvider sInstance;
    private Context mContext;
    private IFlickrApiClient mIFlickrApiClient = new FlickrApiClient();
    private IDataProviderCallbacks<List<IPhoto>> mIDataProviderCallbacks;
    private IRequest<List<IPhoto>> mRequest;
    private List<IPhoto> mIPhotoList=new ArrayList<>();


    private PhotoDataProvider(Context pContext) {
        mContext = pContext.getApplicationContext();
    }

    public static PhotoDataProvider getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PhotoDataProvider.class) {
                sInstance = new PhotoDataProvider(context);
            }
        }
        return sInstance;
    }

    public void searchPhotos(final int pPage, final String query) {
        IRequest<List<IPhoto>> request = new IRequest<List<IPhoto>>() {
            @Override
            public List<IPhoto> runRequest() {
                return mIFlickrApiClient.searchPhotos(pPage, query);
            }
        };
        mRequest = request;
        startLoading(request);
    }

    public void getRecent(final int pPage) {
        IRequest<List<IPhoto>> request = new IRequest<List<IPhoto>>() {
            @Override
            public List<IPhoto> runRequest() {
                return mIFlickrApiClient.getRecent(pPage);
            }
        };
        mRequest = request;
        startLoading(request);
    }

    public void registerCallback(@NonNull IDataProviderCallbacks<List<IPhoto>> pCallback) {
        mIDataProviderCallbacks = pCallback;
    }

    private void startLoading(@NonNull IRequest<List<IPhoto>> pRequest) {
        RequestTask requestTask = new RequestTask();
        requestTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, pRequest);
    }

    public List<IPhoto> getResult() {
        return mIPhotoList;
    }


    private class RequestTask extends AsyncTask<IRequest<List<IPhoto>>, Void, List<IPhoto>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mIDataProviderCallbacks != null) {
                mIDataProviderCallbacks.onStartLoading();
            }
        }

        @Override
        protected List<IPhoto> doInBackground(IRequest<List<IPhoto>>[] pRequests) {
            return pRequests != null ? pRequests[0].runRequest() : null;
        }

        @Override
        protected void onPostExecute(List<IPhoto> pPhotos) {
            super.onPostExecute(pPhotos);
            if (mIDataProviderCallbacks != null && pPhotos != null) {
                mIPhotoList.addAll(pPhotos);
                mIDataProviderCallbacks.onSuccessResult(mIPhotoList);
            }
        }
    }
}
