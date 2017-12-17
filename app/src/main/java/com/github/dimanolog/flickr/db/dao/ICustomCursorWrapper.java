package com.github.dimanolog.flickr.db.dao;

import android.database.Cursor;

/**
 * Created by Dimanolog on 17.12.2017.
 */

public interface ICustomCursorWrapper<T> extends Cursor {
    T get();
}
