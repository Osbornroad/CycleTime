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
    public static final class ProcessEntry implements BaseColumns {
        public static final String TABLE_NAME = "process_table";
        public static final String COLUMN_PROCESS_NAME = "processName";
        public static final String COLUMN_PROCESS_ENABLE = "processEnable";
    }
    public static final class MachineEntry implements BaseColumns {
        public static final String TABLE_NAME = "machine_table";
        public static final String COLUMN_MACHINE_NAME = "machineName";
        public static final String COLUMN_PARENT_PROCESS_ID = "parentProcessName";
        public static final String COLUMN_MACHINE_ENABLE = "machineEnable";
    }
    public static final class PartsEntry implements BaseColumns {
        public static final String TABLE_NAME = "parts_table";
        public static final String COLUMN_PARTS_NAME = "partsName";
        public static final String COLUMN_PARTS_ENABLE = "partsEnable";
    }
}
