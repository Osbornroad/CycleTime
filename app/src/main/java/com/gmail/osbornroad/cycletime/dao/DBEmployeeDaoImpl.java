package com.gmail.osbornroad.cycletime.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15.05.2017.
 */

public class DBEmployeeDaoImpl implements EmployeeDao {

    private Context context;
    private SQLiteDatabase mDb;
    StopWatchDbHelper helper;

    public DBEmployeeDaoImpl(Context context) {
        this.context = context;
        helper = new StopWatchDbHelper(context);
        mDb = helper.getWritableDatabase();
        Utility.insertFakeEmployeeData(mDb);
    }

    @Override
    public Employee get(int id) {
        return null;
    }

    @Override
    public boolean save(Employee employee) {
        return false;
    }

    @Override
    public List<Employee> getAll() {
        Cursor cursor = mDb.query(
                StopWatchContract.EmployeeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME
        );
        List<Employee> allEmployees = new ArrayList();

        cursor.moveToFirst();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(StopWatchContract.EmployeeEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME));
            int enable = cursor.getInt(cursor.getColumnIndex(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE));
            Employee employee = new Employee(id, name, enable == 1);
            allEmployees.add(employee);
        }

        return allEmployees;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void printAll() {

    }
}
