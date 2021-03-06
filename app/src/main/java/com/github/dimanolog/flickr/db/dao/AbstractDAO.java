package com.github.dimanolog.flickr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.github.dimanolog.flickr.db.FlickrDbHelper;
import com.github.dimanolog.flickr.db.dao.cursorwrappers.ICustomCursorWrapper;
import com.github.dimanolog.flickr.util.LogUtil;
import com.github.dimanolog.flickr.util.ReflectUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dimanolog on 10.12.2017.
 */
public abstract class AbstractDAO<T> {
    private static final String ENTITY_DONT_HAVE_ANNOTATION_TABLE = "Entity dont have annotation @Table";

    public static final String DESC = "DESC";
    public static final String ASC = "ASC";

    private final Class<? extends T> mClass;

    protected final String mTableName;
    protected final FlickrDbHelper mFlickrDbHelper;

    protected AbstractDAO(Context pContext, Class<? extends T> pClass) {
        mFlickrDbHelper = new FlickrDbHelper(pContext);
        mClass = pClass;
        String name = ReflectUtil.getTableName(mClass);
        if (name != null) {
            mTableName = name;
        } else {
            throw new RuntimeException(ENTITY_DONT_HAVE_ANNOTATION_TABLE);
        }
    }


    public ICustomCursorWrapper<T> rawQuery(final String sql, @Nullable final String... args) {
        SQLiteDatabase db = mFlickrDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        LogUtil.d(mClass.getSimpleName(), "perform Query: " + sql);

        return wrapCursor(cursor);
    }

    protected ICustomCursorWrapper<T> query(String pWhereClause, String[] pWhereArgs) {
        return query(pWhereClause, pWhereArgs, null);
    }

    protected ICustomCursorWrapper<T> query(String pWhereClause, String[] pWhereArgs, String pOrderBy) {
        SQLiteDatabase db = mFlickrDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                mTableName,
                null,
                pWhereClause,
                pWhereArgs,
                null,
                null,
                pOrderBy
        );

        return wrapCursor(cursor);
    }


    public ICustomCursorWrapper<T> getAll(String pOrderColumn, String pOrderType) {
        return query(null, null, pOrderColumn + " " + pOrderType);
    }


    public Long insert(final T pEntity) {
        if (pEntity != null) {
            final SQLiteDatabase db = mFlickrDbHelper.getWritableDatabase();
            ContentValues values = entityToContentValues(pEntity);
            Long id;
            try {
                db.beginTransaction();

                id = db.insertWithOnConflict(mTableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);

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
                    db.insertWithOnConflict(mTableName, null, value, SQLiteDatabase.CONFLICT_REPLACE);
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
