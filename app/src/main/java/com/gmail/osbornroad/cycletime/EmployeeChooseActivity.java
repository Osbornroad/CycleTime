package com.gmail.osbornroad.cycletime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;

public class EmployeeChooseActivity extends AppCompatActivity implements EmployeeListAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;

    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choose);

        recyclerView = (RecyclerView) findViewById(R.id.rv_employee);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setHasFixedSize(true);


        employeeListAdapter = new EmployeeListAdapter(new EmployeeServiceImpl(new FakeEmployeeDaoImpl()), this);
        recyclerView.setAdapter(employeeListAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
    }
}
