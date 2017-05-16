package com.gmail.osbornroad.cycletime.utility;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 28.04.2017.
 */

public class Utility {

//    private static final ProcessService PROCESS_SERVICE = new FakeProcessServiceImpl(new FakeProcessDaoImpl());
////    private static final MachineService MACHINE_SERVICE = new FakeMachineServiceImpl(new FakeMachineDaoImpl());
//    private static final PartService PART_SERVICE = new FakePartServiceImpl(new FakePartDaoImpl());


//    }
//    public static ProcessService getProcessService() {
//        return PROCESS_SERVICE;
//    }
////    public static MachineService getMachineService() {
////        return MACHINE_SERVICE;
////    }
//    public static PartService getPartService() {
//        return PART_SERVICE;
//    }


    private static final String[] MAIL_ADDRESSES = {"artem.gapenkov@sanoh-rus.com"};
    public static String[] getMailAddresses() {
        return MAIL_ADDRESSES;
    }

    public static void insertFakeEmployeeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> employeeList = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Архипова Анастасия");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        employeeList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Смирнова Анастасия");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        employeeList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Насибулов Дамир");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        employeeList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Иванов Александр");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        employeeList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, "Хижанкова Татьяна");
        cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
        employeeList.add(cv);

        try {
            db.beginTransaction();
            db.delete(StopWatchContract.EmployeeEntry.TABLE_NAME, null, null);
            for (ContentValues c : employeeList) {
                db.insert(StopWatchContract.EmployeeEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }

    public static void insertFakeProcessData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> processList = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, "HPC");
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, 1);
        processList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, "Лазер");
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, 1);
        processList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, "Снятие фаски");
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, 1);
        processList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, "Зачистка");
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, 1);
        processList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, "Уменьшение диаметра");
        cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, 1);
        processList.add(cv);

        try {
            db.beginTransaction();
            db.delete(StopWatchContract.ProcessEntry.TABLE_NAME, null, null);
            for (ContentValues c : processList) {
                db.insert(StopWatchContract.ProcessEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }

    public static void insertFakeMachineData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> machineList = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, "HPC");
        cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 1);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, 1);
        machineList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, "Endform T-6");
        cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 1);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, 1);
        machineList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, "PCO Diesel P32S");
        cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 2);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, 1);
        machineList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, "Assy P32S Cluster #0/2");
        cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 5);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, 1);
        machineList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, "TK324");
        cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 3);
        cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, 1);
        machineList.add(cv);

        try {
            db.beginTransaction();
            db.delete(StopWatchContract.MachineEntry.TABLE_NAME, null, null);
            for (ContentValues c : machineList) {
                db.insert(StopWatchContract.MachineEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }

    public static void insertFakePartsData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> partsList = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, "3834 x 1063PC0  [17338-4CM0A]");
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, 1);
        partsList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, "3188 x 3100PC0  [17510-EM91B]");
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, 1);
        partsList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, "2389 x 3080PC0  [17506-EM90A]");
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, 1);
        partsList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, "752 x 10639C0  [17339-EM92A]");
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, 1);
        partsList.add(cv);

        cv = new ContentValues();
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, "557 x 2047NE0  [46283-4CM0A]");
        cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, 1);
        partsList.add(cv);

        try {
            db.beginTransaction();
            db.delete(StopWatchContract.PartsEntry.TABLE_NAME, null, null);
            for (ContentValues c : partsList) {
                db.insert(StopWatchContract.PartsEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }
}
