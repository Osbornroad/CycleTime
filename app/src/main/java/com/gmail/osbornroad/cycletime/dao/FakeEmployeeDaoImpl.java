package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Employee;

import java.util.List;

/**
 * Created by Натусик on 24.04.2017.
 */

public class FakeEmployeeDaoImpl implements EmployeeDao {

    private List<Employee> employeeList;
    private static int idCounter = 0;

    public FakeEmployeeDaoImpl(List<Employee> employeeList) {
        this.employeeList = employeeList;
        populateEmployeeList();
    }

    private void populateEmployeeList() {
        employeeList.add(new Employee(idCounter, "Макаров", true));
        employeeList.add(new Employee(++idCounter, "Быстров", true));
        employeeList.add(new Employee(++idCounter, "Логутенков", true));
        employeeList.add(new Employee(++idCounter, "Иванов", true));
        employeeList.add(new Employee(++idCounter, "Смирнова", true));
        employeeList.add(new Employee(++idCounter, "Архипова", true));
    }

    @Override
    public Employee get(int id) {
        return null;
    }

    @Override
    public boolean create(Employee employee) {
        return false;
    }

    @Override
    public boolean update(int id, Employee employee) {
        return false;
    }
}
