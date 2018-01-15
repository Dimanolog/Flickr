package com.github.dimanolog.flickr.api.interfaces;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.github.dimanolog.flickr.api.Response;
import com.github.dimanolog.flickr.datamanagers.authorization.UserSession;
import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;

/**
 * Created by Dimanolog on 15.01.2018.
 */

public interface IFlickrApiAuthorizationClient {
    @WorkerThread
    Response<String> getAccesToken(@NonNull UserSession pUserSession);

    @WorkerThread
    IResponse<IResponseStatus> checkToken(@NonNull UserSession pUserSession);

    Uri requestToken();

    Response<String> getRequestToken();

    Uri getUserAuthorizationUri(String pOAuthToken);
}
