package com.github.dimanolog.flickr.util;

import com.github.dimanolog.flickr.db.annotations.Table;

public class ReflectUtil {
    public static String getTableName(Class<?> pClass){
        Table table=pClass.getAnnotation(Table.class);
        if(table!=null){
            return table.value();
        }
        return null;
    }
}
