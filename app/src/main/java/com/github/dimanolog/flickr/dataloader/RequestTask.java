package com.github.dimanolog.flickr.dataloader;

import android.os.AsyncTask;

/**
 * Created by Dimanolog on 08.01.2018.
 */
class RequestTask extends AsyncTask<Void, Void, Void> {
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
