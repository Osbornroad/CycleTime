package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.*;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.service.EmployeeService;

/**
 * Created by User on 25.04.2017.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {

    private EmployeeService employeeService;
    private Employee[] employees;
    final private ListItemClickListener mOnClickListener;
    private Cursor mCursor;

    public EmployeeListAdapter(EmployeeService employeeService, ListItemClickListener mOnClickListener) {
        this.employeeService = employeeService;
        this.employees = employeeService.getEmployeesArray();
        this.mOnClickListener = mOnClickListener;
    }

/*    public EmployeeListAdapter(ListItemClickListener mOnClickListener, Cursor cursor) {
        this.mOnClickListener = mOnClickListener;
        this.mCursor = cursor;
    }*/


    /**
     * Interface ListItemClickListener for clickable of list items
     */
    interface ListItemClickListener {
        void onListItemClick(int clickedEmployeeId);
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_employee;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
/*        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_NAME));
        int enable = mCursor.getInt(mCursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_ENABLE));*/
        holder.bind(employees[position].getId(), employees[position].getEmployeeName());
//        holder.bind(0, name);
    }

    @Override
    public int getItemCount() {
        return employees.length;
    }

/*    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }*/

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listItemEmployeeName;
        int id;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            listItemEmployeeName = (TextView) itemView.findViewById(R.id.tv_employee_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(id);
        }

        public void bind(int employeeId, String employeeName) {
            id = employeeId;
            listItemEmployeeName.setText(employeeName);
        }
    }
}
