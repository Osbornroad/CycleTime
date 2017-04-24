package com.gmail.osbornroad.cycletime.model;

/**
 * Created by Натусик on 24.04.2017.
 */

public class Employee {
    private int id;
    private String employeeName;
    private boolean enable;

    public Employee(int id, String employeeName, boolean enable) {
        this.id = id;
        this.employeeName = employeeName;
        this.enable = enable;
    }

    public int getId() {
        return id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
