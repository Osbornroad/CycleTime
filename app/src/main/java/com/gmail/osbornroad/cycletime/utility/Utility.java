package com.gmail.osbornroad.cycletime.utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 28.04.2017.
 */

public class Utility {

    private static final String[] MAIL_ADDRESSES = {"artem.gapenkov@sanoh-rus.com"};
    public static String[] getMailAddresses() {
        return MAIL_ADDRESSES;
    }

    public static void insertFakeEmployeeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> employeeList = new ArrayList<ContentValues>();

        String[] employeeNamesArray = new String[]{
                "Архипова Анастасия",
                "Смирнова Анастасия",
                "Насибулов Дамир",
                "Иванов Александр",
                "Хижанкова Татьяна",
                "Юшков Яков",
                "Макаров Дмитрий",
                "Николаева Дарья",
                "Быстров Максим",
                "Пшеницын Иван",
                "Милованов Александр",
                "Логутенков Алексей",
                "Богданов Артём",
                "Сергеев Александр"
        };

        for (int i = 0; i < employeeNamesArray.length; i ++) {
            ContentValues cv = new ContentValues();
            cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME, employeeNamesArray[i]);
            cv.put(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE, 1);
            employeeList.add(cv);
        }

        try {
            db.beginTransaction();
            db.delete(StopWatchContract.EmployeeEntry.TABLE_NAME, null, null);
            for (ContentValues c : employeeList) {
                db.insert(StopWatchContract.EmployeeEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            db.endTransaction();
        }
    }

    public static void insertFakeProcessData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

//        List<ContentValues> processList = new ArrayList<ContentValues>();

        String[] processNamesArray = new String[]{
                "HPC",
                "Лазер",
                "Снятие фаски",
                "Зачистка",
                "Уменьшение диаметра",
                "Формовка",
                "PCO",
                "Нарезка SUMI",
                "Печь",
                "Tokai",
                "Гибка",
                "Сборка"
        };

        for (int i = 0; i < processNamesArray.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ORDER_NUMBER, getMaxOrderNumberOfProcesses(db) + 1);
            cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME, processNamesArray[i]);
            cv.put(StopWatchContract.ProcessEntry.COLUMN_PROCESS_ENABLE, 1);
//            processList.add(cv);


        try {
            db.beginTransaction();
//            db.delete(StopWatchContract.ProcessEntry.TABLE_NAME, null, null);
//            for (ContentValues c : processList) {
                db.insert(StopWatchContract.ProcessEntry.TABLE_NAME, null, cv);
//            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            db.endTransaction();
        }
        }
    }

    public static int getMaxOrderNumberOfProcesses(SQLiteDatabase db) {
        if (db == null) {
            return 0;
        }
        int maxOrderNumber = 0;
        Cursor cursor = db.query(StopWatchContract.ProcessEntry.TABLE_NAME,
                new String[]{"MAX(" + StopWatchContract.ProcessEntry.COLUMN_PROCESS_ORDER_NUMBER + ")"},
                null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            maxOrderNumber = cursor.getInt(0);
        }
        cursor.close();
        return maxOrderNumber;
    }


    public static void insertFakeMachineData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }

        List<ContentValues> machineList = new ArrayList<ContentValues>();

        String[] machineNamesArray = new String[]{
                "Assy P32R Cluster #0/1",
                "Assy P32R Cluster #2",
                "Assy P32S Cluster #0/2",
                "Assy P32S Cluster #1",
                "Assy P42M Cluster",
                "Assy P32R/S",
                "Assy P42M ABS (F)",
                "Assy P42M ABS (K)",
                "Tokai P32R",
                "Tokai P32S",
                "FD92",
                "TK390",
                "TK389",
                "TK324",
                "TK323",
                "LR 297",
                "LR 296",
                "LR 252",
                "LR 251",
                "Assy 2800",
                "Add bending 2800",
                "PCO Diesel P32R",
                "PCO Diesel P32S",
                "PCO straight",
                "QCN",
                "Endform T-9",
                "Endform T-8",
                "Endform T-7",
                "Endform T-6",
                "Buffing",
                "Chamfering",
                "Laser",
                "HPC"
        };

        for (int i = 0; i < machineNamesArray.length; i ++) {
            ContentValues cv = new ContentValues();
            cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME, machineNamesArray[i]);
            cv.put(StopWatchContract.MachineEntry.COLUMN_PARENT_PROCESS_ID, 1);
            cv.put(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE, 1);
            machineList.add(cv);
        }

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

        String[] partNameArray = new String[]{
                "322 x 11008CT  [47401-5BF0A]",
                "479 x 3100PC0  [17510-EM91A]",
                "557 x 2047NE0  [46283-4CM0A]",
                "580 x 20478ET  [46240-5BK0A]",
                "588 x 20478ET  [46240-5BF0A]",
                "604 x 2047NE0  [46282-4CM0A]",
                "752 x 10639C0  [17339-EM92A]",
                "759 x 3100PC0  [17510-4CM2C]",
                "959 x 20638ET  [46250-5BK0A]",
                "976 x 20638ET  [46250-5BF0A]",
                "1040 x 20478ET  [46283-5BF0A]",
                "1047 x 20638ET  [46252-5BF0A]",
                "1054 x 20478ET  [46282-5BK0A]",
                "1068 x 20478ET  [46282-5BF0A]",
                "1086 x 20638ET  [46252-5BK0A]",
                "1109 x 2047NE0  [46240-4CM0A]",
                "1122 x 20478ET  [46283-5BK0A]",
                "1143 x 10639C0  [17339-4CM0A]",
                "1364 x 2063NE0  [46250-4CM0A]",
                "1371 x 20478ET  [46242-5BK0A]",
                "1430 x 20478ET  [46242-5BF0A]",
                "1453 x 2063NE0  [46252-4CM0A]",
                "1952 x 2047NE0  [46242-4CM0A]",
                "2385 x 3080PC0  [17506-EM92A]",
                "2389 x 3080PC0  [17506-EM90A]",
                "2450 x 3080PC0  [17506-4CM0A]",
                "2504 x 3100PC0  [17506-EM91A]",
                "2512 x 3080PC0  [17506-5BF0A]",
                "2552 x 3080PC0  [17506-5BK0A]",
                "2560 x 3080PC0  [17506-4CM1A]",
                "2564 x 3100PC0  [17506-4CM2A]",
                "2800 x 3100PC0  [17510-EM91C]",
                "3006 x 2047PED  [46284-EM90A]",
                "3007 x 2047PED  [46284-EM92A]",
                "3067 x 2047PE0  [46284-4CM0A]",
                "3169 x 2047PE0  [46285-5BF0A]",
                "3188 x 3100PC0  [17510-EM91B]",
                "3549 x 3100HC0  [17510-4CM2A]",
                "3707 x 10639C0  [17338-5BF0A]",
                "3707 x 10639C1  [17338-5BF0A]",
                "3759 x 10639C0  [17338-5BK0A]",
                "3774 x 1063PC0  [17338-EM90A]",
                "3834 x 1063PC0  [17338-4CM0A]",
                "4232 x 2047PED  [46285-EM90A]",
                "4233 x 2047PED  [46285-EM92A]",
                "4292 x 2047PE0  [46285-4CM0A]",
                "4723 x 2047PE0  [46284-5BF0A]"
        };

        for(int i = 0; i < partNameArray.length; i ++) {
            ContentValues cv = new ContentValues();
            cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER, getMaxOrderNumberOfPart(db) + 1);
            cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME, partNameArray[i]);
            cv.put(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE, 1);
            partsList.add(cv);
        }

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

    public static int getMaxOrderNumberOfPart(SQLiteDatabase db) {
        if (db == null) {
            return 0;
        }
        int maxOrderNumber = 0;
        Cursor cursor = db.query(StopWatchContract.PartsEntry.TABLE_NAME,
                new String[]{"MAX(" + StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + ")"},
                null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            maxOrderNumber = cursor.getInt(0);
        }
        cursor.close();
        return maxOrderNumber;
    }
}
