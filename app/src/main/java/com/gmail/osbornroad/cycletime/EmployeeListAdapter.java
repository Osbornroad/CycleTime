package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private Resources resources;

    public EmployeeListAdapter(ListItemClickListener mOnClickListener, ListItemLongClickListener mOnLongClickListener, Cursor cursor, Resources resources) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = cursor;
        this.resources = resources;
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

    void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
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
        holder.itemView.setTag(id);
        holder.bind(id, name, enable);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    private int selectedPos = 0;

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView listName;
        Employee employee;
        ImageView visible;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.tv_employee_name);
            visible = (ImageView) itemView.findViewById(R.id.element_hidden_employee);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnLongClickListener.onListItemLongClick(employee);
            v.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight));
            return true;
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(employee);
        }

        public void bind(int employeeId, String employeeName, boolean employeeEnable) {
            employee = new Employee(employeeId, employeeName, employeeEnable);
            listName.setText(employeeName);
//            visible.setVisibility(employee.isEnable() ? View.INVISIBLE : View.VISIBLE);
            if (employee.isEnable()) {
                visible.setVisibility(View.INVISIBLE);
                listName.setTextColor(resources.getColor(R.color.colorPrimary));
            } else {
                visible.setVisibility(View.VISIBLE);
                listName.setTextColor(resources.getColor(android.R.color.darker_gray));
            }
        }


    }
}
