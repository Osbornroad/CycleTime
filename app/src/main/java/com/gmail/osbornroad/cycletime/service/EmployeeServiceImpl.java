package com.gmail.osbornroad.cycletime.service;

import com.gmail.osbornroad.cycletime.dao.EmployeeDao;
import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.model.Employee;

import java.util.Collection;

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
        return employeeDao.save(employee);
    }

    @Override
    public Collection<Employee> getAll() {
        return employeeDao.getAll();
    }

    @Override
    public void delete(int id) {
        employeeDao.delete(id);
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

    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());

        employeeService .printAll();

        Employee employee = new Employee("Сидоров");
        employeeService.save(employee);
        employeeService.printAll();

        Employee employee1 = employeeService.get(3);

        System.out.println();
        System.out.println(employee1);
        System.out.println();

        Employee employee2 = new Employee(employee1.getId(), "Жихарев", false);

        employeeService.save(employee2);

        employeeService.printAll();
    }
}