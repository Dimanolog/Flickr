package com.github.dimanolog.flickr.depencyInjection.component;

import com.github.dimanolog.flickr.datamanagers.PhotoDataManager;
import com.github.dimanolog.flickr.datamanagers.authorization.AuthorizationManager;
import com.github.dimanolog.flickr.datamanagers.comment.CommentsManager;
import com.github.dimanolog.flickr.depencyInjection.modules.ApiModule;

import javax.inject.Singleton;

@dagger.Component(modules = ApiModule.class)
@Singleton
public interface ApiComponent {

    void inject(PhotoDataManager pPhotoDataManager);

    void inject(AuthorizationManager pAuthorizationManager);

    void inject(CommentsManager pCommentsManager);

}
