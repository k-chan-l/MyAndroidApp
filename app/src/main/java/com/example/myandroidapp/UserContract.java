package com.example.myandroidapp;

import android.provider.BaseColumns;

public final class UserContract {
    public static final String DB_NAME="user.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

    /* Inner class that defines the table contents */
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME="Calendar";

        public static final String KEY_NAME = "Title";
        public static final String KEY_MEMO = "Memo";

        public static final String KEY_YEAR = "Year";

        public static final String KEY_MONTH = "Month";

        public static final String KEY_DATE = "Date";

        public static final String KEY_EDM = "Endminute";

        public static final String KEY_EDH = "Endhour";

        public static final String KEY_STM = "Startminute";

        public static final String KEY_STH = "Starthour";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_MEMO + TEXT_TYPE + COMMA_SEP +
                KEY_YEAR + INT_TYPE + COMMA_SEP +
                KEY_MONTH + INT_TYPE + COMMA_SEP +
                KEY_DATE + INT_TYPE + COMMA_SEP +
                KEY_EDM + INT_TYPE + COMMA_SEP +
                KEY_EDH + INT_TYPE + COMMA_SEP +
                KEY_STM + INT_TYPE + COMMA_SEP +
                KEY_STH + INT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
