package com.gmail.osbornroad.cycletime.model;

import java.util.GregorianCalendar;

/**
 * Created by User on 24.04.2017.
 */

public class Sample {
    private int id;
    private GregorianCalendar date;
    private String processName;
    private String machineName;
    private String partName;
    private String workerName;
    private int cycleTime;
    private String comments;

    public Sample(GregorianCalendar date, String processName, String machineName, String partName, String workerName, int cycleTime, String comments) {
        this.date = date;
        this.processName = processName;
        this.machineName = machineName;
        this.partName = partName;
        this.workerName = workerName;
        this.cycleTime = cycleTime;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public String getProcessName() {
        return processName;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getPartName() {
        return partName;
    }

    public String getWorkerName() {
        return workerName;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id=" + id +
                ", date=" + date +
                ", processName='" + processName + '\'' +
                ", machineName='" + machineName + '\'' +
                ", partName='" + partName + '\'' +
                ", workerName='" + workerName + '\'' +
                ", cycleTime=" + cycleTime +
                ", comments='" + comments + '\'' +
                '}';
    }
}
