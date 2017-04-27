package com.gmail.osbornroad.cycletime.model;

/**
 * Created by User on 27.04.2017.
 */
public class Machine {
    private Integer id;
    private final String machineName;
    private final Integer parentProcessId;
    private final boolean enable;

    public Machine(Integer id, String machineName, Integer parentProcessId, boolean enable) {
        this.id = id;
        this.machineName = machineName;
        this.parentProcessId = parentProcessId;
        this.enable = enable;
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

    public String getMachineName() {
        return machineName;
    }

    public Integer getParentProcessId() {
        return parentProcessId;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", machineName='" + machineName + '\'' +
                ", parentProcessId=" + parentProcessId +
                ", enable=" + enable +
                '}';
    }
}
