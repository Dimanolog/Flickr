package com.github.dimanolog.flickr.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.dimanolog.flickr.db.annotations.Column;
import com.github.dimanolog.flickr.db.annotations.Identity;
import com.github.dimanolog.flickr.db.annotations.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dimanolog on 09.12.2017.
 */

public class FlickrDbHelper extends SQLiteOpenHelper {
    private static final String TAG = FlickrDbHelper.class.getSimpleName();
    private static final String TABLE_TEMPLATE =
            "CREATE TABLE IF NOT EXISTS %s (%s) ";
    private static final String ID = "INTEGER PRIMARY KEY NOT NULL";
    private Map<Class<?>, String> classToSqlTypeMap = new HashMap<>();


    public FlickrDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableFromModel(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTableFromModel(SQLiteDatabase db) {
        Class<?>[] modelClasses = ModelClassHolder.getModelClasses();

        String tableName;
        for (Class<?> clazz : modelClasses) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            if (tableAnnotation == null) {
                continue;
            }
            tableName = tableAnnotation.value();
            Field[] fields = clazz.getFields();
            StringBuilder sqlStringBuilder = new StringBuilder();
            addSqlColumns(sqlStringBuilder, fields);
            Log.d(TAG, "creating table: " + tableName);
            createTable(db, String.format(TABLE_TEMPLATE, tableName, sqlStringBuilder.toString()));
        }
    }

    private void createTable(SQLiteDatabase pDb, String pSqlTable) {
        pDb.beginTransaction();
        try {
            pDb.execSQL(pSqlTable);
            pDb.setTransactionSuccessful();
            Log.d(TAG, "create table success, table sql: " + pSqlTable);
        } catch (SQLException pE) {
            Log.e(TAG, "Cant create tabel sql: " + pSqlTable);
        } finally {
            pDb.endTransaction();
        }
    }

    private void addSqlColumns(StringBuilder pStringBuilder, Field[] pFields) {
        Map<Class<?>, String> classToSqlTypeMap = ModelClassHolder.getTypesMap();
        for (Field field : pFields) {
            Column column = field.getAnnotation(Column.class);
            String columnName;
            String sqlType;
            if (column != null) {
                columnName = column.value();
                sqlType = classToSqlTypeMap.get(field.getClass());
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
                sqlType = classToSqlTypeMap.get(field.getClass());
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
