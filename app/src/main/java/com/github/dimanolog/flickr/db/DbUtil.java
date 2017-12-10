package com.github.dimanolog.flickr.db;

import com.github.dimanolog.flickr.model.flickr.Photo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dimanolog on 09.12.2017.
 */

class DbUtil {
    static Class<?>[] getModelClasses() {
        return new Class[]{
                Photo.class
        };
    }

    static Map<Class<?>, String> getTypesMap() {
        Map<Class<?>, String> classToSqlTypeMap = new HashMap<>();
        classToSqlTypeMap.put(Integer.class, "INTEGER");
        classToSqlTypeMap.put(Long.class, "INTEGER");
        classToSqlTypeMap.put(Date.class, "INTEGER");
        classToSqlTypeMap.put(String.class, "TEXT");
        classToSqlTypeMap.put(Double.class, "REAL");

        return classToSqlTypeMap;
    }
}
