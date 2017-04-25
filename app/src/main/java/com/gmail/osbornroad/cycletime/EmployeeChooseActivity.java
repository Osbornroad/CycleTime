package com.gmail.osbornroad.cycletime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;

public class EmployeeChooseActivity extends AppCompatActivity {

    private static final int NUM_LIST_ITEMS = 100;
    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choose);

        recyclerView = (RecyclerView) findViewById(R.id.rv_employee);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setHasFixedSize(true);


        employeeListAdapter = new EmployeeListAdapter(new EmployeeServiceImpl(new FakeEmployeeDaoImpl()));
        recyclerView.setAdapter(employeeListAdapter);
    }
}
