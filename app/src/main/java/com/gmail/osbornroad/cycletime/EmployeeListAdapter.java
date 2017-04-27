package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.service.EmployeeService;

/**
 * Created by User on 25.04.2017.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> {

    private EmployeeService employeeService;
    private Employee[] employees;
    final private ListItemClickListener mOnClickListener;

    public EmployeeListAdapter(EmployeeService employeeService, ListItemClickListener mOnClickListener) {
        this.employeeService = employeeService;
        this.employees = employeeService.getEmployeesArray();
        this.mOnClickListener = mOnClickListener;
    }

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
        holder.bind(employees[position].getId(), employees[position].getEmployeeName());
    }

    @Override
    public int getItemCount() {
        return employees.length;
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listItemEmployeeId;
        TextView listItemEmployeeName;
        int id;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            listItemEmployeeId = (TextView) itemView.findViewById(R.id.tv_employee_id);
            listItemEmployeeName = (TextView) itemView.findViewById(R.id.tv_employee_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(id);
        }

        public void bind(int employeeId, String employeeName) {
            id = employeeId;
            listItemEmployeeId.setText(String.valueOf(employeeId));
            listItemEmployeeName.setText(employeeName);
        }
    }
}
