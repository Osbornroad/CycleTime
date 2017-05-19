package com.gmail.osbornroad.cycletime;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;
import com.gmail.osbornroad.cycletime.model.Employee;

public class EmployeesFragment extends Fragment implements EmployeeListAdapter.ListItemClickListener, EmployeeListAdapter.ListItemLongClickListener, NavigationFragment {

    private static final int NUM_LIST_ITEMS = 100;
    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    private final int FRAGMENT_ID = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public EmployeesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_employee_choose, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_employee);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * Get adapter, set to RecyclerView
         */

        mainActivity = (MainActivity) getActivity();

        Cursor cursor = getAllEmployees();
        employeeListAdapter = new EmployeeListAdapter(this, this, cursor);
        recyclerView.setAdapter(employeeListAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(Employee employee) {
        mainActivity.selectedEmployee = employee;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }

    @Override
    public void onListItemLongClick(Employee employee) {
        Toast.makeText(mainActivity.getApplicationContext(), employee.getEmployeeName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    private Cursor getAllEmployees() {
        return mainActivity.mDb.query(
                StopWatchContract.EmployeeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME
        );
    }
}
