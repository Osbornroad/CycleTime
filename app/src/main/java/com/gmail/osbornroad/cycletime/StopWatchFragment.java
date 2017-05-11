package com.gmail.osbornroad.cycletime;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeMachineDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakePartDaoImpl;
import com.gmail.osbornroad.cycletime.dao.FakeProcessDaoImpl;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.model.Machine;
import com.gmail.osbornroad.cycletime.model.Part;
import com.gmail.osbornroad.cycletime.model.Process;
import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeMachineServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakePartServiceImpl;
import com.gmail.osbornroad.cycletime.service.FakeProcessServiceImpl;
import com.gmail.osbornroad.cycletime.service.MachineService;
import com.gmail.osbornroad.cycletime.service.PartService;
import com.gmail.osbornroad.cycletime.service.ProcessService;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopWatchFragment extends Fragment {

    public StopWatchFragment() {
        // Required empty public constructor
    }

    private final int MSG_START = 0;
    private final int MSG_RESUME = 2;
    private final int MSG_STOP = 3;
    private final int MSG_UPDATE = 4;

    MainActivity mainActivity;

    /**
     * StopWatch is singletone
     */
    private StopWatch stopWatch;
    /**
     * mStarted == true after Start button pressed until Stop pressed
     */
    private boolean mStarted;
    /**
     * mInProgress == true after Resume/Start button, Continue counting until Reset pressed
     */
    private boolean mInProgress;
    /**
     * REFRESH_RATE is constant for RecyclerView item setting
     */
    private final int REFRESH_RATE = 10;
    /**
     * StopWatch interface
     */
    private TextView timeRunningView;
    private Button startButton;
    private Button resetButton;
    /**
     * Employee setting interface
     * lineEmployee contains employeeInfo
     */
    private EmployeeService employeeService;
    //    private LinearLayout lineEmployee;
    private TextView employeeInfo;
    private Employee selectedEmployee;
    /**
     * Process setting interface
     */
    private ProcessService processService;
    private TextView processInfo;
    private Process selectedProcess;
    /**
     * Machine setting interface
     */
    private MachineService machineService;
    private TextView machineInfo;
    private Machine selectedMachine;
    /**
     * Part setting interface
     */
    private PartService partService;
    private TextView partInfo;
    private Part selectedPart;

    private EditText partQuantity;

    public boolean ismInProgress() {
        return mInProgress;
    }

    /**
     * Getters
     */
    public boolean ismStarted() {        return mStarted;    }
    public StopWatch getStopWatch() {
        return stopWatch;
    }
    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }
    public Process getSelectedProcess() {
        return selectedProcess;
    }
    public Machine getSelectedMachine() {
        return selectedMachine;
    }
    public Part getSelectedPart() {
        return selectedPart;
    }
    public EditText getPartQuantity() {
        return partQuantity;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public ProcessService getProcessService() {
        return processService;
    }

    public MachineService getMachineService() {
        return machineService;
    }

    public PartService getPartService() {
        return partService;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_stop_watch, container, false);

        /**
         * StopWatch main functional creating
         */
        stopWatch = StopWatch.getStopWatch();
        timeRunningView = (TextView) rootView.findViewById(R.id.time_running_view);
        startButton = (Button) rootView.findViewById(R.id.start_button);
        resetButton = (Button) rootView.findViewById(R.id.reset_button);
        /**
         * Employee setting interface creating
         */
        employeeService = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());
//        lineEmployee = (LinearLayout) findViewById(R.id.line_employee);
        employeeInfo = (TextView) rootView.findViewById(R.id.employee_name_data);
        employeeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmployeeChooseActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        /**
         * Process setting interface creating
         */
        processService = new FakeProcessServiceImpl(new FakeProcessDaoImpl());
        processInfo = (TextView) rootView.findViewById(R.id.process_name_data);
        processInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProcessChooseActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        /**
         * Machine setting interface creating
         */
        machineService = new FakeMachineServiceImpl(new FakeMachineDaoImpl());
        machineInfo = (TextView) rootView.findViewById(R.id.machine_name_data);
        machineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MachineChooseActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        /**
         * Part setting interface creating
         */
        partService = new FakePartServiceImpl(new FakePartDaoImpl());
        partInfo = (TextView) rootView.findViewById(R.id.part_name_data);
        partInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PartChooseActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        partQuantity = (EditText) rootView.findViewById(R.id.part_qty_data);

        mainActivity = (MainActivity) getActivity();

        setInfoWithoutSavedInstanceState();

        /**
         * Setting saved data
         */
        setSavedInfo(savedInstanceState);
        /**
         * Refresh Stopwatch interface after activity creation
         */
        stopWatchResume();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStarted) {
                    if (!mInProgress) {
                        mHandler.sendEmptyMessage(MSG_START);
                        mStarted = true;
                        startButton.setText(R.string.button_stop);
                        mInProgress = true;
                    } else {
                        mHandler.sendEmptyMessage(MSG_RESUME);
                        mStarted = true;
                        startButton.setText(R.string.button_stop);
                    }
                } else {
                    mHandler.sendEmptyMessage(MSG_STOP);
                    mStarted = false;
                    startButton.setText(R.string.button_resume);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStarted) {
                    mInProgress = false;
                    timeRunningView.setText("00:00:00");
                    startButton.setText(R.string.button_start);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        mainActivity.mStartedInActivity = mStarted;
        mainActivity.mInProgressInActivity = mInProgress;
        mainActivity.selectedEmployeeInActivity = selectedEmployee;
        mainActivity.selectedProcessInActivity = selectedProcess;
        mainActivity.selectedMachineInActivity = selectedMachine;
        mainActivity.selectedPartInActivity = selectedPart;
        int quantity;
        try {
            quantity = Integer.parseInt(partQuantity.getText().toString());
        } catch (NumberFormatException e) {
            quantity = 0;
        }
        mainActivity.partQuantityInActivity = quantity;
        super.onPause();
    }

    private void setInfoWithoutSavedInstanceState() {
        mStarted = mainActivity.mStartedInActivity;
        mInProgress = mainActivity.mInProgressInActivity;
        selectedEmployee = mainActivity.selectedEmployeeInActivity;
        selectedProcess = mainActivity.selectedProcessInActivity;
        selectedMachine = mainActivity.selectedMachineInActivity;
        selectedPart = mainActivity.selectedPartInActivity;

        int quantity = mainActivity.partQuantityInActivity;
        if (quantity > 0) {
            partQuantity.setText(String.valueOf(quantity));
        }

        if (selectedEmployee != null) {
            employeeInfo.setText(selectedEmployee.getEmployeeName());
            employeeInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }

        if (selectedProcess != null) {
            processInfo.setText(selectedProcess.getProcessName());
            processInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }

        if (selectedMachine != null) {
            machineInfo.setText(selectedMachine.getMachineName());
            machineInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }

        if (selectedPart != null) {
            partInfo.setText(selectedPart.getPartName());
            partInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
    }

    /**
     * Setting saved data
     */
    private void setSavedInfo(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mStarted")) {
                mStarted = savedInstanceState.getBoolean("mStarted");
            }
            if (savedInstanceState.containsKey("mInProgress")) {
                mInProgress = savedInstanceState.getBoolean("mInProgress");
            }
            if (savedInstanceState.containsKey("employeeId")) {
                int employeeId = savedInstanceState.getInt("employeeId");
                selectedEmployee = employeeService.get(employeeId);
                employeeInfo.setText(selectedEmployee.getEmployeeName());
                employeeInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
            }
            if (savedInstanceState.containsKey("processId")) {
                int processId = savedInstanceState.getInt("processId");
                selectedProcess = processService.get(processId);
                processInfo.setText(selectedProcess.getProcessName());
                processInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
            }
            if (savedInstanceState.containsKey("machineId")) {
                int machineId = savedInstanceState.getInt("machineId");
                selectedMachine = machineService.get(machineId);
                machineInfo.setText(selectedMachine.getMachineName());
                machineInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
            }
            if (savedInstanceState.containsKey("partId")) {
                int partId = savedInstanceState.getInt("partId");
                selectedPart = partService.get(partId);
                partInfo.setText(selectedPart.getPartName());
                partInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
            }
            if (savedInstanceState.containsKey("timeRunningView")) {
                timeRunningView.setText(savedInstanceState.getString("timeRunningView"));
            }
        }
    }
    /**
     * Save data before activity termination
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mStarted", mStarted);
        outState.putBoolean("mInProgress", mInProgress);
        if (selectedEmployee != null) {
            outState.putInt("employeeId", selectedEmployee.getId());
        }
        if (selectedProcess != null) {
            outState.putInt("processId", selectedProcess.getId());
        }
        if (selectedMachine != null) {
            outState.putInt("machineId", selectedMachine.getId());
        }
        if (selectedPart != null) {
            outState.putInt("partId", selectedPart.getId());
        }
        outState.putString("timeRunningView", timeRunningView.getText().toString());
    }

    /**
     * Setting data, which has been received from child activities:
     * EmployeeChooseActivity
     * ProcessChooseActivity
     * MachineChooseActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (data.hasExtra("employeeId")) {
            int employeeId = data.getExtras().getInt("employeeId");
            selectedEmployee = employeeService.get(employeeId);
            employeeInfo.setText(selectedEmployee.getEmployeeName());
            employeeInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (data.hasExtra("processId")) {
            int processId = data.getExtras().getInt("processId");
            selectedProcess = processService.get(processId);
            processInfo.setText(selectedProcess.getProcessName());
            processInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (data.hasExtra("machineId")) {
            int machineId = data.getExtras().getInt("machineId");
            selectedMachine = machineService.get(machineId);
            machineInfo.setText(selectedMachine.getMachineName());
            machineInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (data.hasExtra("partId")) {
            int partId = data.getExtras().getInt("partId");
            selectedPart = partService.get(partId);
            partInfo.setText(selectedPart.getPartName());
            partInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
    }
    /**
     * Handler managing of Stopwatch displaying
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START:
                    stopWatch.onStart();
                    mHandler.sendEmptyMessage(MSG_UPDATE);
                    break;
                case MSG_UPDATE:
                    timeRunningView.setText(stopWatch.getFormattedElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE, REFRESH_RATE);
                    break;
                case MSG_STOP:
                    mHandler.removeMessages(MSG_UPDATE);
                    stopWatch.onStop();
                    timeRunningView.setText(stopWatch.getFormattedElapsedTime());
                    break;
                case MSG_RESUME:
                    stopWatch.onResume();
                    mHandler.sendEmptyMessage(MSG_UPDATE);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * Refresh Stopwatch interface after activity creation
     */
    private void stopWatchResume() {

        timeRunningView.setText(stopWatch.getFormattedElapsedTime());

        if (mInProgress) {
            if (mStarted) {
                mHandler.sendEmptyMessage(MSG_UPDATE);
                startButton.setText(R.string.button_stop);
            } else {
                startButton.setText(R.string.button_resume);
            }
        }
    }
}

