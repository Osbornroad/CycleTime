package com.gmail.osbornroad.cycletime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27.04.2017.
 */

public class Process implements Parcelable {
    private Integer id;
    private final String processName;
    private final boolean enable;
//    private final List<Integer> machineList;

    public Process(Integer id, String processName, boolean enable/*, List<Integer> machineList*/) {
        this.id = id;
        this.processName = processName;
        this.enable = enable;
//        this.machineList = machineList;
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

/*    public List<Integer> getMachineList() {
        return machineList;
    }*/

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", processName='" + processName + '\'' +
                ", enable=" + enable +
//                ", machineList=" + machineList +
                '}';
    }
    /**
     * Parcelable part
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(processName);
        dest.writeByte((byte) (enable ? 1 : 0));
    }

    public static final Creator<Process> CREATOR = new Creator<Process>() {
        @Override
        public Process createFromParcel(Parcel in) {
            return new Process(in);
        }

        @Override
        public Process[] newArray(int size) {
            return new Process[size];
        }
    };

    protected Process(Parcel in) {
        id = in.readInt();
        processName = in.readString();
        enable = (in.readInt() == 1);
    }

}
