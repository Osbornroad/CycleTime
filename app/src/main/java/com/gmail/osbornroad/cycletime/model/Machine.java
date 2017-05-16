package com.gmail.osbornroad.cycletime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27.04.2017.
 */
public class Machine implements Parcelable {
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
        dest.writeString(machineName);
        dest.writeInt(parentProcessId);
        dest.writeInt(enable ? 1 : 0);
    }

    public static final Creator<Machine> CREATOR = new Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel in) {
            return new Machine(in);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };

    protected Machine(Parcel in) {
        id = in.readInt();
        machineName = in.readString();
        parentProcessId = in.readInt();
        enable = (in.readInt() == 1);
    }
}
