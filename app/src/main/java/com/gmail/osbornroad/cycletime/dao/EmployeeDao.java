package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Employee;

import java.util.List;

/**
 * Created by Натусик on 24.04.2017.
 */

public interface EmployeeDao {

    Employee get(int id);

    boolean save(Employee employee);

    List<Employee> getAll();

    void delete(int id);

    void printAll();
}
