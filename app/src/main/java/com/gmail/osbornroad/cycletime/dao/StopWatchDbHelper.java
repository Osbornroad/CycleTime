package com.gmail.osbornroad.cycletime.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gmail.osbornroad.cycletime.dao.StopWatchContract.*;
import com.gmail.osbornroad.cycletime.utility.Utility;

/**
 * Created by User on 15.05.2017.
 */

public class StopWatchDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "stopwatch.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 49;

    public StopWatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=on");

        //create Employee table
        final String SQL_CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + EmployeeEntry.TABLE_NAME + " (" +
                EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " INTEGER NOT NULL, " +
                EmployeeEntry.COLUMN_EMPLOYEE_NAME + " TEXT NOT NULL, " +
                EmployeeEntry.COLUMN_EMPLOYEE_ENABLE + " INTEGER NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_EMPLOYEE_TABLE);

        //Process table
        final String SQL_CREATE_PROCESS_TABLE = "CREATE TABLE " + ProcessEntry.TABLE_NAME + " (" +
                ProcessEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProcessEntry.COLUMN_PROCESS_ORDER_NUMBER + " INTEGER NOT NULL, " +
                ProcessEntry.COLUMN_PROCESS_NAME + " TEXT NOT NULL, " +
                ProcessEntry.COLUMN_PROCESS_ENABLE + " INTEGER NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_PROCESS_TABLE);

        //Machine table
        final String SQL_CREATE_MACHINE_TABLE = "CREATE TABLE " + MachineEntry.TABLE_NAME + " (" +
                MachineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " INTEGER NOT NULL, " +
                MachineEntry.COLUMN_MACHINE_NAME + " TEXT NOT NULL, " +
                MachineEntry.COLUMN_PARENT_PROCESS_ID + " INTEGER NOT NULL, " +
                MachineEntry.COLUMN_MACHINE_ENABLE + " INTEGER NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_MACHINE_TABLE);

        //Parts table
        final String SQL_CREATE_PARTS_TABLE = "CREATE TABLE " + PartsEntry.TABLE_NAME + " (" +
                PartsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " INTEGER NOT NULL, " +
                PartsEntry.COLUMN_PARTS_NAME + " TEXT NOT NULL, " +
                PartsEntry.COLUMN_PARTS_ENABLE + " INTEGER NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_PARTS_TABLE);

        //Sample table
        final String SQL_CREATE_SAMPLE_TABLE = "CREATE TABLE " + SampleEntry.TABLE_NAME + " (" +
                SampleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SampleEntry.COLUMN_EMPLOYEE_ID + " INTEGER, " +
                SampleEntry.COLUMN_PROCESS_ID + " INTEGER, " +
                SampleEntry.COLUMN_MACHINE_ID + " INTEGER, " +
                SampleEntry.COLUMN_PART_ID + " INTEGER, " +
                SampleEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                SampleEntry.COLUMN_START_DATE_TIME + " STRING NOT NULL, " +
                SampleEntry.COLUMN_DURATION + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + SampleEntry.COLUMN_EMPLOYEE_ID + ") REFERENCES " + EmployeeEntry.TABLE_NAME + "(" + EmployeeEntry._ID + "), " +
                "FOREIGN KEY (" + SampleEntry.COLUMN_PROCESS_ID + ") REFERENCES " + ProcessEntry.TABLE_NAME + "(" + ProcessEntry._ID + "), " +
                "FOREIGN KEY (" + SampleEntry.COLUMN_MACHINE_ID + ") REFERENCES " + MachineEntry.TABLE_NAME + "(" + MachineEntry._ID + "), " +
                "FOREIGN KEY (" + SampleEntry.COLUMN_PART_ID + ") REFERENCES " + PartsEntry.TABLE_NAME + "(" + PartsEntry._ID + ")" +
                "); ";
        db.execSQL(SQL_CREATE_SAMPLE_TABLE);

        Utility.insertFakeEmployeeData(db);
        Utility.insertFakeProcessData(db);
        Utility.insertFakeMachineData(db);
        Utility.insertFakePartsData(db);
    }

    /**
     * TODO Update onUpgrade
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProcessEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MachineEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PartsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SampleEntry.TABLE_NAME);
        onCreate(db);
    }
}
