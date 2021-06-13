package com.example.myandroidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, UserContract.DB_NAME, null, UserContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(UserContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(UserContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public void insertUserBySQL(String title, String memo, int year, int month, int date, int endminute, int endhour, int startminute, int starthour) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%d', '%d', '%d', '%d', '%d', '%d', '%d')",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    UserContract.Users.KEY_NAME,
                    UserContract.Users.KEY_MEMO,
                    UserContract.Users.KEY_YEAR,
                    UserContract.Users.KEY_MONTH,
                    UserContract.Users.KEY_DATE,
                    UserContract.Users.KEY_EDM,
                    UserContract.Users.KEY_EDH,
                    UserContract.Users.KEY_STM,
                    UserContract.Users.KEY_STH,
                    title,
                    memo,
                    year,
                    month,
                    date,
                    endminute,
                    endhour,
                    startminute,
                    starthour);

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }
    public void deleteUserBySQL(int _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %d",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public void updateUserBySQL(int _id, String title, String memo, int year, int month, int date, int endminute, int endhour, int startminute, int starthour) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s', %s = '%d', %s = '%d', %s = '%d', %s = '%d', %s = '%d', %s = '%d', %s = '%d' WHERE %s = %s",
                    UserContract.Users.TABLE_NAME,
                    UserContract.Users.KEY_NAME, title,
                    UserContract.Users.KEY_MEMO, memo,
                    UserContract.Users.KEY_YEAR, year,
                    UserContract.Users.KEY_MONTH, month,
                    UserContract.Users.KEY_DATE, date,
                    UserContract.Users.KEY_EDM, endminute,
                    UserContract.Users.KEY_EDH, endhour,
                    UserContract.Users.KEY_STM, startminute,
                    UserContract.Users.KEY_STH, starthour,

                    UserContract.Users._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public Cursor getCurentDayBySQL(int year, int month, int day) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME + " Where Month = " + month + " and Year = " + year + " and Date = " + day;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public Cursor getIDBySQL(int id) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME + " Where _id = " + id;
        return getReadableDatabase().rawQuery(sql, null);
    }

    public Cursor getTimeBySQL(int year, int month, int day, int sth) {
        String sql = "Select * FROM " + UserContract.Users.TABLE_NAME + " Where Month = " + month + " and Year = " + year + " and Date = " + day + " and Starthour = " + sth;
        return getReadableDatabase().rawQuery(sql, null);
    }





}
