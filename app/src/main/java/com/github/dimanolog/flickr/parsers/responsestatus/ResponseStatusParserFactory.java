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

package com.github.dimanolog.flickr.parsers.responsestatus;

import com.github.dimanolog.flickr.model.flickr.interfaces.IResponseStatus;
import com.github.dimanolog.flickr.parsers.interfaces.IParser;

/**
 * Created by Dimanolog on 11.01.2018.
 */

public class ResponseStatusParserFactory {

    public IParser<IResponseStatus> getGsonParser() {
        return new ResponseStatusGsonParser();
    }
}
