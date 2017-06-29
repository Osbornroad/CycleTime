package com.gmail.osbornroad.cycletime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 27.04.2017.
 */

public class Part implements Parcelable {
    private Integer id;
    private Integer orderNumber;
    private final String partName;
    private final boolean enable;

    public Part(Integer id, Integer orderNumber, String partName, boolean enable) {
        this.id = id;
        this.orderNumber = orderNumber;
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

    public Integer getOrderNumber() {
        return orderNumber;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(orderNumber);
        dest.writeString(partName);
        dest.writeInt(enable ? 1 : 0);
    }

    public static final Creator<Part> CREATOR = new Creator<Part>() {
        @Override
        public Part createFromParcel(Parcel in) {
            return new Part(in);
        }

        @Override
        public Part[] newArray(int size) {
            return new Part[size];
        }
    };
    protected Part(Parcel in) {
        id = in.readInt();
        orderNumber = in.readInt();
        partName = in.readString();
        enable = (in.readInt() == 1);
    }
}
