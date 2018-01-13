package com.github.dimanolog.flickr.threading;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.datamanagers.IRequest;

public class RequestExecutor{
    public static void executeRequestSerial(@NonNull IRequest pRequest) {
        RequestTask requestTask = new RequestTask(pRequest);
        requestTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null);
    }
}
