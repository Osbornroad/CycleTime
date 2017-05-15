package com.gmail.osbornroad.cycletime.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gmail.osbornroad.cycletime.dao.StopWatchContract.*;

/**
 * Created by User on 15.05.2017.
 */

public class StopWatchDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "stopwatch.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public StopWatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create Employee database
        final String SQL_CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + EmployeeEntry.TABLE_NAME + " (" +
                EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EmployeeEntry.COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL, " +
                EmployeeEntry.COLUMN_EMPLOYEE_ENABLE + " INTEGER NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_EMPLOYEE_TABLE);
    }

    /**
     * TODO Update onUpgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeEntry.TABLE_NAME);
        onCreate(db);
    }
}
