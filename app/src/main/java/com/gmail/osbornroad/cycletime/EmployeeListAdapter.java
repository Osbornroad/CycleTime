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

    public EmployeeListAdapter(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.employees = employeeService.getEmployeesArray();
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_employee;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {

        holder.bind(employees[position].getEmployeeName());

//        TextView textView = holder.listItemNumberView;
//        TextView employeeTextView = (TextView) textView.findViewById(R.id.tv_item_number);
//        textView.setText(employees[position].getEmployeeName());
    }

    @Override
    public int getItemCount() {
        return employees.length;
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView listItemNumberView;
//        TextView viewHolderIndex;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            listItemNumberView = (TextView) itemView.findViewById(R.id.tv_item_number);
//            viewHolderIndex = (TextView) itemView.findViewById(R.id.tv_view_holder_instance)
        }

        public void bind(String text) {
            listItemNumberView.setText(text);
        }
    }

}
