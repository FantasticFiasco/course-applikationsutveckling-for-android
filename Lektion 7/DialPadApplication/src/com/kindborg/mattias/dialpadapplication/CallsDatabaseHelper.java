package com.kindborg.mattias.dialpadapplication;

import android.content.*;
import android.database.sqlite.*;

public class CallsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "calls.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table " +
        Calls.TABLE_NAME +
        "(" +
        Calls._ID + " integer primary key autoincrement, " +
        Calls.DATETIME + " text not null," +
        Calls.NUMBER + " text not null" +
        ")";

    public CallsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Calls.TABLE_NAME);
        onCreate(db);
    }

    public abstract class Calls {

        public static final String TABLE_NAME = "calls";
        public static final String _ID = "id";
        public static final String DATETIME = "datetime";
        public static final String NUMBER = "number";
    }
}