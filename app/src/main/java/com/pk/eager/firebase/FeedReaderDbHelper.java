package com.pk.eager.firebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by therangersolid on 10/16/17.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static String USER = "user";
    public static String BOT = "bot";

    public static String TABLE_NAME = "messages";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "id" + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1," +
                    "message" + " TEXT NOT NULL," +
                    "sender" + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Message.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static ArrayList<String> read(String selection,
                                         String[] selectionArgs, SQLiteDatabase db) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = { "id",
                "message", "sender" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "id" + " ASC";

        Cursor c = db.query(FeedReaderDbHelper.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection/* selection */, // The columns for the WHERE clause
                // (May
                // used for checking the deleted
                // items)
                selectionArgs/* selectionArgs */, // The values for the WHERE
                // clause
                null, // don't group the rows
                null, // don't filter by row groups
                sortOrder // The sort order
        );
        ArrayList<String> masterItems = new ArrayList<String>();
        boolean hasItems = c.moveToFirst();
        for (; hasItems;) {
            if (hasItems) {
                masterItems.add(c.getString(c
                        .getColumnIndexOrThrow("id")));
                masterItems.add(c.getString(c
                        .getColumnIndexOrThrow("message")));
                masterItems.add(c.getString(c
                        .getColumnIndexOrThrow("sender")));
            }
            hasItems = c.moveToNext();
        }
        c.close();
        return masterItems;
    }
}