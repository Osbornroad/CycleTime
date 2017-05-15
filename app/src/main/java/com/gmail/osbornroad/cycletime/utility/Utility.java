package com.gmail.osbornroad.cycletime.utility;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeMachineDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakePartDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeProcessDaoImpl;
import com.gmail.osbornroad.cycletime.dao.StopWatchContract;
import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeMachineServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakePartServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeProcessServiceImpl;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 28.04.2017.
 */

public class Utility {
    private static final EmployeeService EMPLOYEE_SERVICE = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());
    private static final ProcessService PROCESS_SERVICE = new FakeProcessServiceImpl(new FakeProcessDaoImpl());
    private static final MachineService MACHINE_SERVICE = new FakeMachineServiceImpl(new FakeMachineDaoImpl());
    private static final PartService PART_SERVICE = new FakePartServiceImpl(new FakePartDaoImpl());
    private static final String[] MAIL_ADDRESSES = {"artem.gapenkov@sanoh-rus.com"};

    public static EmployeeService getEmployeeService() {
        return EMPLOYEE_SERVICE;
    }
    public static ProcessService getProcessService() {
        return PROCESS_SERVICE;
    }
    public static MachineService getMachineService() {
        return MACHINE_SERVICE;
    }
    public static PartService getPartService() {
        return PART_SERVICE;
    }
    public static String[] getMailAddresses() {
        return MAIL_ADDRESSES;
    }

    public static void insertFakeEmployeeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Архипова Анастасия");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Смирнова Анастасия");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Насибулов Дамир");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Иванов Александр");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        list.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Хижанкова Татьяна");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        list.add(cv);

        try {
            db.beginTransaction();
            db.delete(StopWatchContract.EmployeeEntry.TABLE_NAME, null, null);
            for (ContentValues c : list) {
                db.insert(StopWatchContract.EmployeeEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }
}
