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

    interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
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

        holder.bind(employees[position].getId(), employees[position].getEmployeeName());

//        TextView textView = holder.listItemEmployeeId;
//        TextView employeeTextView = (TextView) textView.findViewById(R.id.tv_item_number);
//        textView.setText(employees[position].getEmployeeName());
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
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(id);
//            mOnClickListener.onListItemClick(clickedPosition);
        }

        public void bind(int employeeId, String employeeName) {
            id = employeeId;
            listItemEmployeeId.setText(String.valueOf(employeeId));
            listItemEmployeeName.setText(employeeName);
        }
    }

}
