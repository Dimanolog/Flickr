package com.github.dimanolog.flickr.threading;

import android.os.AsyncTask;

import com.github.dimanolog.flickr.datamanagers.IRequest;

/**
 * Created by Dimanolog on 08.01.2018.
 */
class RequestTask extends AsyncTask<Void, Void, Void> {
    private IRequest mIRequest;

    RequestTask(IRequest pIRequest) {
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
