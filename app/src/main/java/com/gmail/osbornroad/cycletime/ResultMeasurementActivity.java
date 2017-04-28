package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;
import com.gmail.osbornroad.cycletime.utility.Utility;

public class ResultMeasurementActivity extends AppCompatActivity {

    private EmployeeService employeeService = Utility.getEmployeeService();
    private ProcessService processService = Utility.getProcessService();
    private MachineService machineService = Utility.getMachineService();
    private PartService partService = Utility.getPartService();

    private int employeeId;

    private Intent intent;

    TextView employeeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_measurement);

        intent = getIntent();

        setSampleData();
    }

    private void setSampleData() {
        if (intent.hasExtra("employeeId")) {
            employeeName = (TextView) findViewById(R.id.employee_name_in_result);
            employeeId = intent.getIntExtra("employeeId", 0);
            employeeName.setText(employeeService.get(this.employeeId).getEmployeeName());
        }

    }
}
