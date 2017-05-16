package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.dao.EmployeeDao;
import com.gmail.osbornroad.cycletime.model.Employee;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 25.04.2017.
 */

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDao employeeDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public Employee get(int id) {
        return employeeDao.get(id);
    }

    @Override
    public boolean save(Employee employee) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = employeeDao.getAll();
        Collections.sort(employees, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getEmployeeName().compareTo(o2.getEmployeeName());
            }
        });
        return employees;
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printAll() {
        employeeDao.printAll();
    }

    @Override
    public Employee[] getEmployeesArray() {

        Employee[] employees = getAll().toArray(new Employee[getAll().size()]);
        return employees;
    }

}
