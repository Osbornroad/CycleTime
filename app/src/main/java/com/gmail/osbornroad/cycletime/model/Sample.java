package com.gmail.osbornroad.cycletime.model;

import java.util.Calendar;

/**
 * Created by User on 24.04.2017.
 */

public class Sample {
    private int id;
    private Integer employeeId;
    private Integer processId;
    private Integer machineId;
    private Integer partId;
    private int quantity;
    private Calendar startDateTime;
    private int duration; //in sec

    public Sample(int id, Integer employeeId, Integer processId,
                  Integer machineId, Integer partId, int quantity,
                  Calendar startDateTime, int duration) {
        this.id = id;
        this.employeeId = employeeId;
        this.processId = processId;
        this.machineId = machineId;
        this.partId = partId;
        this.quantity = quantity;
        this.startDateTime = startDateTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Integer getProcessId() {
        return processId;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public Integer getPartId() {
        return partId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Calendar getStartDateTime() {
        return startDateTime;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", processId=" + processId +
                ", machineId=" + machineId +
                ", partId=" + partId +
                ", quantity=" + quantity +
                ", startDateTime=" + startDateTime +
                ", duration=" + duration +
                '}';
    }
}
