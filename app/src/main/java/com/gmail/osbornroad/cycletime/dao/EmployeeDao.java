package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Employee;

/**
 * Created by Натусик on 24.04.2017.
 */

public interface EmployeeDao {

    Employee get(int id);

    boolean create(Employee employee);

    boolean update(int id, Employee employee);
}
