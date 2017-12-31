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
    private static final String ENTITY_DONT_HAVE_ANNOTATION_TABLE = "Entity dont have annotation @Table";

    private final FlickrDbHelper mFlickrDbHelper;
    private final Class<? extends T> mClass;
    private final String mTableName;

    AbstractDAO(Context pContext, Class<? extends T> pClass) {
        mFlickrDbHelper = new FlickrDbHelper(pContext);
        mClass = pClass;
        String name = ReflectUtil.getTableName(mClass);
        if (name != null) {
            mTableName = name;
        } else {
            throw new RuntimeException(ENTITY_DONT_HAVE_ANNOTATION_TABLE);
        }
    }

    public ICustomCursorWrapper<T> rawQuery(final String sql, final String... args) {
        SQLiteDatabase db = mFlickrDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);

        return wrapCursor(cursor);
    }

    public ICustomCursorWrapper<T> getAll() {
        return rawQuery("select * from " + mTableName, null);
    }


    public Long insert(final T pEntity) {
        if (pEntity != null) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            ContentValues values = entityToContentValues(pEntity);
            Long id;
            try {
                db.beginTransaction();

                id = db.insert(mTableName, null, values);

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            return id;
        }

        return null;
    }

    public Integer bulkInsert(final Collection<T> pEntities) {
        List<ContentValues> valuesList = entityCollectionToContentValues(pEntities);
        if (!valuesList.isEmpty()) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            int count = 0;
            try {
                db.beginTransaction();

                for (final ContentValues value : valuesList) {
                    db.insert(mTableName, null, value);
                    count++;
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return count;
        }
        return null;
    }


    public int delete(final String sql, final String[] args) {
        final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(mTableName, sql, args);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    private List<ContentValues> entityCollectionToContentValues(Collection<T> pEntities) {
        List<ContentValues> valueList = new LinkedList<>();
        for (T entity : pEntities) {
            valueList.add(entityToContentValues(entity));
        }
        return valueList;
    }

    protected abstract ICustomCursorWrapper<T> wrapCursor(Cursor pCursor);

    protected abstract ContentValues entityToContentValues(T pEntity);
}
