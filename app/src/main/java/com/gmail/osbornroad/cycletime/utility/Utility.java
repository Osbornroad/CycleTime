package com.gmail.osbornroad.cycletime.utility;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeMachineDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakePartDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeProcessDaoImpl;
import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeMachineServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakePartServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeProcessServiceImpl;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;

/**
 * Created by User on 28.04.2017.
 */

public class Utility {
    private static final EmployeeService EMPLOYEE_SERVICE = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());
    private static final ProcessService PROCESS_SERVICE = new FakeProcessServiceImpl(new FakeProcessDaoImpl());
    private static final MachineService MACHINE_SERVICE = new FakeMachineServiceImpl(new FakeMachineDaoImpl());
    private static final PartService PART_SERVICE = new FakePartServiceImpl(new FakePartDaoImpl());

    private static final String[] MAIL_ADDRESSES = {
            "artem.gapenkov@sanoh-rus.com",
            "maksim.tkachenko@sanoh-rus.com"};

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
}
