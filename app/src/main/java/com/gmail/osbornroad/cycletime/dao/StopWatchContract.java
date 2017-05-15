package com.gmail.osbornroad.cycletime.dao;

import android.provider.BaseColumns;

/**
 * Created by User on 15.05.2017.
 */

public class StopWatchContract {
    public static final class EmployeeEntry implements BaseColumns {
        public static final String TABLE_NAME = "employee_table";
        public static final String COLUMN_EMPLOYEE_NAME = "employeeName";
        public static final String COLUMN_EMPLOYEE_ENABLE = "employeeEnable";
    }
}
