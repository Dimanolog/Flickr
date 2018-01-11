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

package com.github.dimanolog.flickr.model.flickr.interfaces;

/**
 * Created by dimanolog on 11.01.18.
 */

public interface ICommentary {
    Long getId();

    void setId(Long pId);

    String getAuthorName();

    void setAuthorName(String pAuthorName);

    Integer getAuthorIsDeleted();

    void setAuthorIsDeleted(Integer pAuthorIsDeleted);

    Integer getIconServer();

    void setIconServer(Integer pIconServer);

    Integer getIconFarm();

    void setIconFarm(Integer pIconFarm);

    Long getDateCreate();

    void setDateCreate(Long pDateCreate);

    String getPermalink();

    void setPermalink(String pPermalink);

    String getPathAlias();

    void setPathAlias(String pPathAlias);

    String getRealName();

    void setRealName(String pRealName);

    String getContent();

    void setContent(String pContent);

    String getAvatarUrl();
}
