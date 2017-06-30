package com.gmail.osbornroad.cycletime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Натусик on 24.04.2017.
 */

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Employee implements Parcelable {
    private Integer id;
    private Integer orderNumber;
    private final String employeeName;
    private final boolean enable;

    public Employee(Integer id, Integer orderNumber, String employeeName, boolean enable) {
        this.id = id;
        this.orderNumber = orderNumber;
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

    public Integer getOrderNumber() {
        return orderNumber;
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
                ", orderNumber=" + orderNumber +
                ", employeeName='" + employeeName + '\'' +
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
        dest.writeInt(orderNumber);
        dest.writeString(employeeName);
        dest.writeInt(enable ? 1 : 0);
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {

        @Override
        public Employee createFromParcel(Parcel source) {
            return new Employee(source);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public Employee(Parcel source) {
        id = source.readInt();
        orderNumber = source.readInt();
        employeeName = source.readString();
        enable = (source.readInt() == 1);
    }
}
