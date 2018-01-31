
package com.github.dimanolog.flickr.depencyInjection.modules;


import android.support.annotation.NonNull;

import com.github.dimanolog.flickr.api.FlickrApiAuthorizationClient;
import com.github.dimanolog.flickr.api.FlickrApiCommentaryClient;
import com.github.dimanolog.flickr.api.FlickrApiPhotoClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiAuthorizationClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiCommentaryClient;
import com.github.dimanolog.flickr.api.interfaces.IFlickrApiPhotoClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    @NonNull
    @Singleton
    IFlickrApiPhotoClient providePhotoApi(){
        return new FlickrApiPhotoClient();
    }
    @Provides
    @NonNull
    @Singleton
    IFlickrApiCommentaryClient provideCommentaryApi(){
        return new FlickrApiCommentaryClient();
    }

    @Provides
    @NonNull
    @Singleton
    IFlickrApiAuthorizationClient provideAuthorizationClient(){
        return new FlickrApiAuthorizationClient();
    }
}
