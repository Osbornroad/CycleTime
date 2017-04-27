package com.gmail.osbornroad.cycletime.model;

/**
 * Created by User on 27.04.2017.
 */

public class Part {
    private Integer id;
    private final String partName;
    private final boolean enable;

    public Part(Integer id, String partName, boolean enable) {
        this.id = id;
        this.partName = partName;
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

    public String getPartName() {
        return partName;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", partName='" + partName + '\'' +
                ", enable=" + enable +
                '}';
    }
}
