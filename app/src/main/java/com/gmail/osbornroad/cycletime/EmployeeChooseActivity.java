/*
package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;
import com.gmail.osbornroad.cycletime.dao.StopWatchDbHelper;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.utility.Utility;

*/
/**
 * Implement EmployeeListAdapter.ListItemClickListener for clickable of list items
 *//*


public class EmployeeChooseActivity extends AppCompatActivity implements EmployeeListAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;
    protected SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choose);
        */
/**
         * Get RecyclerView by Id
         * Set LayoutManager
         * Set dividers
         *//*

        recyclerView = (RecyclerView) findViewById(R.id.rv_employee);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        StopWatchDbHelper helper = new StopWatchDbHelper(this);
        mDb = helper.getWritableDatabase();
        Utility.insertFakeEmployeeData(mDb);
        */
/**
         * Get adapter, set to RecyclerView
         *//*

        Cursor cursor = getAllEmployees();
        employeeListAdapter = new EmployeeListAdapter(this, cursor);
        recyclerView.setAdapter(employeeListAdapter);
    }
    */
/**
     * Send result to StopWatchActivity of clicked employee
     *//*


    @Override
    public void onListItemClick(Employee employee) {
        Intent intent = new Intent();
        intent. putExtra("employee", employee);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Cursor getAllEmployees() {
        return mDb.query(
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
*/
