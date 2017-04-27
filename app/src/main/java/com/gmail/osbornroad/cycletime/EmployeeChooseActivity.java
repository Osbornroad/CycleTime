package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;

/**
 * Implement EmployeeListAdapter.ListItemClickListener for clickable of list items
 */

public class EmployeeChooseActivity extends AppCompatActivity implements EmployeeListAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choose);
        /**
         * Get RecyclerView by Id
         * Set LayoutManager
         * Set dividers
         */
        recyclerView = (RecyclerView) findViewById(R.id.rv_employee);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
    }
    /**
     * Send result to StopWatchActivity of clicked employee list item
     * @param clickedEmployeeId Id of clicked employee list item
     */
    @Override
    public void onListItemClick(int clickedEmployeeId) {
        Intent intent = new Intent();
        intent. putExtra("employeeId", clickedEmployeeId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
