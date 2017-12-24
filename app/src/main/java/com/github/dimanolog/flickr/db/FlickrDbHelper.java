package com.github.dimanolog.flickr.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Dimanolog on 09.12.2017.
 */

public class FlickrDbHelper extends SQLiteOpenHelper {
    private static final String TAG = FlickrDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "Flickr.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s) ";
    private static final String ID = "INTEGER PRIMARY KEY NOT NULL";
    private static final String DELETE_TABLE_TEMPLATE = "DROP TABLE IF EXISTS '%s'";

    public FlickrDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Class<?>[] modelClasses = DbUtil.getModelClasses();
        for (Class<?> clazz : modelClasses) {
            String sql = getCreateTableSqlString(clazz);
            if (!TextUtils.isEmpty(sql)) {
                createTable(sql, db);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            Class<?>[] modelClasses = DbUtil.getModelClasses();
            for (Class<?> clazz : modelClasses) {
                String tableName = ReflectUtil.getTableName(clazz);
                db.execSQL(String.format(DELETE_TABLE_TEMPLATE, tableName));

                onCreate(db);
            }
        }
    }

    private String getCreateTableSqlString(Class<?> pClass) {
        String tableName = ReflectUtil.getTableName(pClass);
        if (tableName == null) {
            return null;
        }
        Field[] fields = pClass.getDeclaredFields();
        StringBuilder sqlStringBuilder = new StringBuilder();
        addSqlColumns(sqlStringBuilder, fields);

        return String.format(TABLE_TEMPLATE, tableName, sqlStringBuilder.toString());
    }

    private void createTable(String pSqlTable,  SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(pSqlTable);
            db.setTransactionSuccessful();
            Log.d(TAG, "create table success, table sql: " + pSqlTable);
        } catch (SQLException pE) {
            Log.e(TAG, "cant create tabel sql: " + pSqlTable);
        } finally {
            db.endTransaction();
        }
    }

    private void addSqlColumns(StringBuilder pStringBuilder, Field[] pFields) {
        Map<Class<?>, String> classToSqlTypeMap = DbUtil.getTypesMap();
        for (Field field : pFields) {
            Column column = field.getAnnotation(Column.class);
            String columnName;
            String sqlType;
            if (column != null) {
                columnName = column.value();
                sqlType = classToSqlTypeMap.get(field.getType());
                if (sqlType != null) {
                    pStringBuilder.append(columnName)
                            .append(" ")
                            .append(sqlType)
                            .append(",");
                }
                continue;
            }
            Identity identity = field.getAnnotation(Identity.class);
            if (identity != null) {
                sqlType = classToSqlTypeMap.get(field.getType());
                if (sqlType != null) {
                    pStringBuilder.append(identity.value())
                            .append(" ")
                            .append(ID)
                            .append(",");
                }
            }
        }
        pStringBuilder.setLength(pStringBuilder.length() - 1);
    }
}
