package com.kindborg.mattias.dialpadapplication;

import java.util.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class CallsDataSource {

    private static final String[] allColumns =
    {
        CallsDatabaseHelper.Calls._ID,
        CallsDatabaseHelper.Calls.DATETIME,
        CallsDatabaseHelper.Calls.NUMBER
    };

    private final CallsDatabaseHelper callsDatabaseHelper;

    private SQLiteDatabase database;

    public CallsDataSource(Context context) {
        callsDatabaseHelper = new CallsDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = callsDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        callsDatabaseHelper.close();
    }

    public void create(String dateTime, String number) {
        ContentValues values = new ContentValues();

        values.put(CallsDatabaseHelper.Calls.DATETIME, dateTime);
        values.put(CallsDatabaseHelper.Calls.NUMBER, number);

        database.insert(CallsDatabaseHelper.Calls.TABLE_NAME, null, values);
    }

    public List<Call> getAll() {
        List<Call> comments = new ArrayList<Call>();

        Cursor cursor = database.query(
            CallsDatabaseHelper.Calls.TABLE_NAME,
            allColumns,
            null,
            null,
            null,
            null,
            null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            comments.add(cursorToCall(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return comments;
    }

    private Call cursorToCall(Cursor cursor) {
        String dateTime = cursor.getString(1);
        String number = cursor.getString(2);

        return new Call(dateTime, number);
    }
}
