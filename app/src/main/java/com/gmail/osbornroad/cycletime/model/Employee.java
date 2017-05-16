package com.gmail.osbornroad.cycletime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Натусик on 24.04.2017.
 */

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Employee implements Parcelable {
    private Integer id;
    private final String employeeName;
    private final boolean enable;

    public Employee(String employeeName) {
        this(null, employeeName, true);
    }

    public Employee(String employeeName, boolean enable) {
        this(null, employeeName, enable);
    }

    public Employee(Integer id, String employeeName, boolean enable) {
        this.id = id;
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
                ", employeeName='" + employeeName + '\'' +
                ", enable=" + enable +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcelable part
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(employeeName);
        dest.writeInt(enable ? 1 : 0);
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {

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
        employeeName = source.readString();
        enable = (source.readInt() == 1);
    }
}
