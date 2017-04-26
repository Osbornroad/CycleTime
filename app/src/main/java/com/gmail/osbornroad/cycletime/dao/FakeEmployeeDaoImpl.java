package com.gmail.osbornroad.cycletime.dao;

import com.gmail.osbornroad.cycletime.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Натусик on 24.04.2017.
 */

public class FakeEmployeeDaoImpl implements EmployeeDao {

    private List<Employee> employeeList;
    private AtomicInteger idCounter = new AtomicInteger(100);


    public FakeEmployeeDaoImpl() {
        this.employeeList = new ArrayList<>();
        populateEmployeeList();
    }

    private void populateEmployeeList() {
        employeeList.add(new Employee(getNewId(), "Макаров", true));
        employeeList.add(new Employee(getNewId(), "Быстров", true));
        employeeList.add(new Employee(getNewId(), "Логутенков", true));
        employeeList.add(new Employee(getNewId(), "Иванов", true));
        employeeList.add(new Employee(getNewId(), "Смирнова", true));
        employeeList.add(new Employee(getNewId(), "Архипова", true));
    }

    private int getNewId() {
        return idCounter.getAndIncrement();
    }

    @Override
    public Employee get(int id) {
        for (Employee employee : employeeList) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    @Override
    public boolean save(Employee employee) {
        if (employee.isNew()) {
            employee.setId(getNewId());
            return employeeList.add(employee);
        } else {
            Iterator<Employee> iterator = employeeList.iterator();
            while(iterator.hasNext()) {
                Employee nextEmployee = iterator.next();
                if (employee.getId() == nextEmployee.getId()) {
                    iterator.remove();
                }
            }
            return employeeList.add(employee);
        }
    }

    @Override
    public Collection<Employee> getAll() {
        return employeeList;
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Operation delete in EmployeeDao not supported");
    }

    @Override
    public void printAll() {
        List<Employee> list = new ArrayList<>(getAll());
        Collections.sort(list, new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getId() - o2.getId();
            }
        });
        System.out.println();
        for (Employee employee : list) {
            System.out.println(employee);
        }
        System.out.println();
        System.out.println("*********************************************************");
    }

    public static void main(String[] args) {
        EmployeeDao employeeDao = new FakeEmployeeDaoImpl();

        employeeDao.printAll();

        Employee employee = new Employee("Петров");
        employeeDao.save(employee);
        employeeDao.printAll();

        Employee employee1 = employeeDao.get(2);

        System.out.println();
        System.out.println(employee1);
        System.out.println();

        Employee employee2 = new Employee(employee1.getId(), "Николаева", false);

        employeeDao.save(employee2);

        employeeDao.printAll();
    }
}
