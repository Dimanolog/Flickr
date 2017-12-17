package com.github.dimanolog.flickr.db;

import com.github.dimanolog.flickr.model.flickr.Photo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dimanolog on 09.12.2017.
 */

class DbUtil {

    private static final String SQL_INT = "INTEGER";
    private static final String SQL_STRING = "TEXT";
    private static final String SQL_DOUBLE = "REAL";

    static Class<?>[] getModelClasses() {
        return new Class[]{
                Photo.class
        };
    }

    static Map<Class<?>, String> getTypesMap() {
        Map<Class<?>, String> classToSqlTypeMap = new HashMap<>();
        classToSqlTypeMap.put(Integer.class, SQL_INT);
        classToSqlTypeMap.put(Long.class, SQL_INT);
        classToSqlTypeMap.put(Date.class, SQL_INT);
        classToSqlTypeMap.put(String.class, SQL_STRING);
        classToSqlTypeMap.put(Double.class, SQL_DOUBLE);

        return classToSqlTypeMap;
    }
}
