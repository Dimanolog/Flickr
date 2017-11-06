package com.github.dimanolog.flickr.dataloader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.github.dimanolog.flickr.api.FlickrApiClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiClient;
import com.github.dimanolog.flickr.model.flickr.IPhoto;

import java.util.List;


public class PhotoLoader extends AsyncTaskLoader<List<IPhoto>> {


    private IFlickrApiClient mIFlickrApiClient = new FlickrApiClient();
    private IRequest<List<IPhoto>> mRequest;
    private List<IPhoto> mIPhotoList;

    public PhotoLoader(Context context) {
        super(context);
    }

    public void searchPhotos(final int pPage, final String query) {
        mRequest = new IRequest<List<IPhoto>>() {
            @Override
            public List<IPhoto> runRequest() {
                return mIFlickrApiClient.searchPhotos(pPage, query);
            }
        };
        super.onForceLoad();
    }

    public void getRecent(final int pPage) {
        mRequest = new IRequest<List<IPhoto>>() {
            @Override
            public List<IPhoto> runRequest() {
                return mIFlickrApiClient.getRecent(pPage);
            }
        };
        super.onForceLoad();
    }

    @Override
    public void deliverResult(List<IPhoto> data) {
        mIPhotoList = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mIPhotoList != null) {
            deliverResult(mIPhotoList);
        }
           forceLoad();
        }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


    @Override
    public void onCanceled(List<IPhoto> apps) {
        super.onCanceled(apps);

    }


    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mIPhotoList != null) {
            mIPhotoList = null;
        }
    }




    @Override
    public List<IPhoto> loadInBackground() {
        if (mRequest != null) {
            return mRequest.runRequest();
        }
        throw new IllegalStateException("mRequest == null");
    }

  /*  private  class RequestTask extends AsyncTask<Void, Void, List<IPhoto>> {*/
    /*    @Override
        protected List<IPhoto> doInBackground(Void... voids) {
            return mRequest.runRequest();
        }

        @Override
        protected void onPostExecute(List<IPhoto> photoList) {
            super.onPostExecute(photoList);
            deliverResult(photoList);
        }
    }*/
}
