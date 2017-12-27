/*
 * Copyright (c) 2017 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.api.interfaces;

/**
 * Created by Dimanolog on 19.11.2017.
 */

public interface IResponse<T> {
    T getResult();
    Throwable getError();
    boolean isError();
}
