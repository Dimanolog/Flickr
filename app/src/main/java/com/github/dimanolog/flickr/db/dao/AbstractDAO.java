package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.dimanolog.flickr.db.FlickrDbHelper;
import com.github.dimanolog.flickr.util.ReflectUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dimanolog on 10.12.2017.
 */
public abstract class AbstractDAO<T> {
    private static final String ENTITY_DONT_HAVE_ANNOTATION_TABLE = "Entity dont have annotation table";

    private FlickrDbHelper mFlickrDbHelper;
    private Class<T> mClass;

    AbstractDAO(Context pContext, Class<T> pClass) {
        mFlickrDbHelper = new FlickrDbHelper(pContext);
        mClass = pClass;
    }

    public T rawQuery(final String sql, final String... args) {
        SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return cursorToEntity(cursor);
    }


    public long insert(final ContentValues values) {
        final String name = ReflectUtil.getTableName(mClass);
        if (name != null) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            long id;
            try {
                db.beginTransaction();

                id = db.insert(name, null, values);

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            return id;
        } else {
            throw new RuntimeException(ENTITY_DONT_HAVE_ANNOTATION_TABLE);
        }
    }


    public int bulkInsert(final Collection<T> entities) {
        final String name = ReflectUtil.getTableName(mClass);
        List<ContentValues> valuesList = entityCollectionToContentValues(entities);
        if (name != null && !valuesList.isEmpty()) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            int count = 0;
            try {
                db.beginTransaction();

                for (final ContentValues value : valuesList) {
                    db.insert(name, null, value);
                    count++;
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return count;
        } else {
            throw new RuntimeException(ENTITY_DONT_HAVE_ANNOTATION_TABLE);
        }
    }


    public int delete(final String sql) {
        final String name = ReflectUtil.getTableName(mClass);

        if (name != null) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            int count = 0;
            try {
                db.beginTransaction();
                count = db.delete(name, sql, args);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return count;
        } else {
            throw new RuntimeException(ENTITY_DONT_HAVE_ANNOTATION_TABLE);
        }
    }

    private List<ContentValues> entityCollectionToContentValues(Collection<T> pEntities) {
        List<ContentValues> valueList = new LinkedList<>();
        for (T entity : pEntities) {
            valueList.add(entityToContentValues(entity));
        }
        return valueList;
    }

    protected abstract T cursorToEntity(Cursor pCursor);

    protected abstract ContentValues entityToContentValues(T pEntity);
}
