package com.gmail.osbornroad.cycletime.model;

/**
 * Created by Натусик on 24.04.2017.
 */

public class Employee {
    private Integer id;
    private final String employeeName;
    private final boolean enable;

    public Employee(String employeeName) {
        this(null, employeeName, true);
    }

    public Employee(String employeeName, boolean enable) {
        this(null, employeeName, enable);
    }

    public Employee(Integer id, String employeeName, boolean enable) {
        this.id = id;
        this.employeeName = employeeName;
        this.enable = enable;
    }

    public boolean isNew() {
        return id == null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employeeName='" + employeeName + '\'' +
                ", enable=" + enable +
                '}';
    }
}
