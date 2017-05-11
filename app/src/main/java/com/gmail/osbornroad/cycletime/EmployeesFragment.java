package com.gmail.osbornroad.cycletime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;

public class EmployeesFragment extends Fragment implements EmployeeListAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;


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
        employeeListAdapter = new EmployeeListAdapter(new EmployeeServiceImpl(new FakeEmployeeDaoImpl()), this);
        recyclerView.setAdapter(employeeListAdapter);

        mainActivity = (MainActivity) getActivity();

        return rootView;
    }

    @Override
    public void onListItemClick(int clickedEmployeeId) {
/*        Intent intent = new Intent();
        intent. putExtra("employeeId", clickedEmployeeId);
        setResult(RESULT_OK, intent);
        finish();*/
        mainActivity.setSelectedEmployee(clickedEmployeeId);
        Toast.makeText(getActivity().getApplicationContext(), "Choosen ID: " + clickedEmployeeId, Toast.LENGTH_SHORT).show();
    }

}
