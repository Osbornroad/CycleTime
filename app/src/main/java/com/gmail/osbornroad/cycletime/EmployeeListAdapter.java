package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.EmployeeEntry;
import com.gmail.osbornroad.cycletime.model.Employee;

/**
 * Created by User on 25.04.2017.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private ListItemLongClickListener mOnLongClickListener;
    private Cursor mCursor;

    public EmployeeListAdapter(ListItemClickListener mOnClickListener, ListItemLongClickListener mOnLongClickListener, Cursor cursor) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = cursor;
    }

    /**
     * Interface ListItemClickListener for clickable of list items
     */
    interface ListItemClickListener {
        void onListItemClick(Employee employee);
    }

    interface ListItemLongClickListener {
        void onListItemLongClick(Employee employee);
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
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int id = mCursor.getInt(mCursor.getColumnIndex(EmployeeEntry._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_NAME));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(EmployeeEntry.COLUMN_EMPLOYEE_ENABLE)) == 1;
        holder.bind(id, name, enable);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView listItemEmployeeName;
        Employee employee;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            listItemEmployeeName = (TextView) itemView.findViewById(R.id.tv_employee_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnLongClickListener.onListItemLongClick(employee);
            return false;
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(employee);
        }

        public void bind(int employeeId, String employeeName, boolean employeeEnable) {
            employee = new Employee(employeeId, employeeName, employeeEnable);
            listItemEmployeeName.setText(employeeName);
        }


    }
}
