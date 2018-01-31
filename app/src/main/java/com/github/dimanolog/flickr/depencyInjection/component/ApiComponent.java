/*
 * Copyright (c) 2018 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.depencyInjection.component;

import com.github.dimanolog.flickr.datamanagers.PhotoDataManager;
import com.github.dimanolog.flickr.datamanagers.authorization.AuthorizationManager;
import com.github.dimanolog.flickr.datamanagers.comment.CommentsManager;
import com.github.dimanolog.flickr.depencyInjection.modules.ApiModule;

import javax.inject.Singleton;

/**
 * Created by dimanolog on 31.01.18.
 */
@dagger.Component(modules = ApiModule.class)
@Singleton
public interface ApiComponent {

    void inject(PhotoDataManager pPhotoDataManager);

    void inject(AuthorizationManager pAuthorizationManager);

    void inject(CommentsManager pCommentsManager);

}
