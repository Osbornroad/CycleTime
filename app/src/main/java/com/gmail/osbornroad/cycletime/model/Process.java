package com.gmail.osbornroad.cycletime.model;

import java.util.List;

/**
 * Created by User on 27.04.2017.
 */

public class Process {
    private Integer id;
    private final String processName;
    private final boolean enable;
    private final List<Machine> machineList;

    public Process(Integer id, String processName, boolean enable, List<Machine> machineList) {
        this.id = id;
        this.processName = processName;
        this.enable = enable;
        this.machineList = machineList;
    }

    public boolean isNew() {
        return id == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public boolean isEnable() {
        return enable;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", processName='" + processName + '\'' +
                ", enable=" + enable +
                ", machineList=" + machineList +
                '}';
    }
}
