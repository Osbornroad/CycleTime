package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;
import com.gmail.osbornroad.cycletime.utility.Utility;

public class ResultMeasurementActivity extends AppCompatActivity {

    private ProcessService processService = Utility.getProcessService();
    private MachineService machineService = Utility.getMachineService();
    private PartService partService = Utility.getPartService();

    private Employee employee;
    private int processId;
    private int machineId;
    private int partId;
    private int partQuantity;
    private int resultStopWatch;
    private int cycleTime;

    private Intent intent;

    private TextView employeeNameDisplay;
    private TextView processNameDisplay;
    private TextView machineNameDisplay;
    private TextView partNameDisplay;
    private TextView partQuantityDisplay;
    private TextView resultStopWatchDisplay;
    private TextView cycleTimeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_measurement);

        intent = getIntent();
        setSampleData();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(this, "Back button pressed", Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);
                if(getSupportFragmentManager().getBackStackEntryCount()>0)
                    getSupportFragmentManager().popBackStack();
                return true;
            case R.id.action_mail:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, Utility.getMailAddresses());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Cycletime result for " + processNameDisplay.getText() + " " + partNameDisplay.getText());
                emailIntent.putExtra(Intent.EXTRA_TEXT,
                        getResources().getString(R.string.cycle_time_result) + " " + "\t" + cycleTimeDisplay.getText() + "\n" +
                                "========================================" + "\n" +
                                getResources().getString(R.string.stopwatch_result) + " " + "\t" + resultStopWatchDisplay.getText() + "\n" +
                                getResources().getString(R.string.qty_parts) + " " + "\t" + partQuantityDisplay.getText() + "\n" +
                                "========================================" + "\n" +
                                getResources().getString(R.string.name_part_number) + " " + "\t" + partNameDisplay.getText() + "\n" +
                                getResources().getString(R.string.name_machine) + " " + "\t" + machineNameDisplay.getText() + "\n" +
                                getResources().getString(R.string.name_process) + " " + "\t" + processNameDisplay.getText() + "\n" +
                                getResources().getString(R.string.name_employee) + " " + "\t" + employeeNameDisplay.getText()
                );
                emailIntent.setType("message/rfc822");
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                else {
                    Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSampleData() {

        employeeNameDisplay = (TextView) findViewById(R.id.employee_name_in_result);
        processNameDisplay = (TextView) findViewById(R.id.process_name_in_result);
        machineNameDisplay = (TextView) findViewById(R.id.machine_name_in_result);
        partNameDisplay = (TextView) findViewById(R.id.part_name_in_result);
        partQuantityDisplay = (TextView) findViewById(R.id.part_qty_in_result);
        resultStopWatchDisplay = (TextView) findViewById(R.id.total_time_in_result);
        cycleTimeDisplay = (TextView) findViewById(R.id.cycle_time_in_result);

        if (intent.hasExtra("selectedEmployee")) {
            employee = intent.getParcelableExtra("selectedEmployee");
            employeeNameDisplay.setText(employee.getEmployeeName());
            employeeNameDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (intent.hasExtra("processId")) {
            processId = intent.getIntExtra("processId", 0);
            processNameDisplay.setText(processService.get(this.processId).getProcessName());
            processNameDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (intent.hasExtra("machineId")) {
            machineId = intent.getIntExtra("machineId", 0);
            machineNameDisplay.setText(machineService.get(this.machineId).getMachineName());
            machineNameDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (intent.hasExtra("partId")) {
            partId = intent.getIntExtra("partId", 0);
            partNameDisplay.setText(partService.get(this.partId).getPartName());
            partNameDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (intent.hasExtra("partQuantity")) {
            partQuantity = intent.getIntExtra("partQuantity", 0);
            partQuantityDisplay.setText(String.valueOf(partQuantity));
            partQuantityDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (intent.hasExtra("resultStopWatch")) {
            resultStopWatch = intent.getIntExtra("resultStopWatch", 0);

            int hours = (int) (resultStopWatch / 3600);
            int minutes = (int) ((resultStopWatch % 3600) / 60);
            int seconds = (int) (resultStopWatch % 60);
            String output = String.format("%02d:%02d:%02d", hours, minutes, seconds);

//            String resultForDisplay = String.valueOf(resultStopWatch) + " sec";
            resultStopWatchDisplay.setText(output);
            resultStopWatchDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if ((partQuantity != 0) && (resultStopWatch != 0)) {
            cycleTime = resultStopWatch % partQuantity == 0 ? resultStopWatch / partQuantity : resultStopWatch / partQuantity + 1;
            String cycleTimeForDisplay = String.valueOf(cycleTime) + " sec";
            cycleTimeDisplay.setText(cycleTimeForDisplay);
            cycleTimeDisplay.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
    }
}
