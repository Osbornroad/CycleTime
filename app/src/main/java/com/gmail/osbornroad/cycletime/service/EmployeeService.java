package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.model.Employee;

import java.util.Collection;

/**
 * Created by User on 25.04.2017.
 */

public interface EmployeeService {

    Employee get(int id);

    boolean save(Employee employee);

    Collection<Employee> getAll();

    void delete(int id);

    void printAll();

    Employee[] getEmployeesArray();
}
