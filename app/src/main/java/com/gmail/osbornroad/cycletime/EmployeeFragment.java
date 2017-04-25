package com.gmail.osbornroad.cycletime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeFragment extends Fragment {


    public EmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView employeeRecyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_employee, container, false);

        EmployeeListAdapter adapter = new EmployeeListAdapter(new EmployeeServiceImpl(new FakeEmployeeDaoImpl()));
        employeeRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        employeeRecyclerView.setLayoutManager(layoutManager);

        // Inflate the layout for this fragment
        return employeeRecyclerView;
    }

}
