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
//    private boolean mStarted;
    /**
     * mInProgress == true after Resume/Start button, Continue counting until Reset pressed
     */
//    private boolean mInProgress;
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
//    private EmployeeService employeeService;
    //    private LinearLayout lineEmployee;
    private TextView employeeInfo;
//    private Employee selectedEmployee;
    /**
     * Process setting interface
     */
    private TextView processInfo;
    /**
     * Machine setting interface
     */
//    private MachineService machineService;
    private TextView machineInfo;
//    private Machine selectedMachine;
    /**
     * Part setting interface
     */
//    private PartService partService;
    private TextView partInfo;
//    private Part selectedPart;

    private EditText partQuantity;

//    public boolean ismInProgress() {
//        return mInProgress;
//    }
/*
    *//**
     * Getters
     *//*
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
    }*/

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
//        employeeService = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());
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
//        processService = new FakeProcessServiceImpl(new FakeProcessDaoImpl());
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
//        machineService = new FakeMachineServiceImpl(new FakeMachineDaoImpl());
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
//        partService = new FakePartServiceImpl(new FakePartDaoImpl());
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
//        setSavedInfo(savedInstanceState);
        /**
         * Refresh Stopwatch interface after activity creation
         */
        stopWatchResume();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.mStarted) {
                    if (!mainActivity.mInProgress) {
                        mHandler.sendEmptyMessage(MSG_START);
                        mainActivity.mStarted = true;
                        startButton.setText(R.string.button_stop);
                        mainActivity.mInProgress = true;
                    } else {
                        mHandler.sendEmptyMessage(MSG_RESUME);
                        mainActivity.mStarted = true;
                        startButton.setText(R.string.button_stop);
                    }
                } else {
                    mHandler.sendEmptyMessage(MSG_STOP);
                    mainActivity.mStarted = false;
                    startButton.setText(R.string.button_resume);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainActivity.mStarted) {
                    mainActivity.mInProgress = false;
                    timeRunningView.setText("00:00:00");
                    startButton.setText(R.string.button_start);
                }
            }
        });

        return rootView;
    }
    @Override
    public void onPause() {
/*        mainActivity.mStarted = mStarted;
        mainActivity.mInProgress = mInProgress;
        mainActivity.selectedEmployee = selectedEmployee;
        mainActivity.selectedProcess = selectedProcess;
        mainActivity.selectedMachine = selectedMachine;
        mainActivity.selectedPart = selectedPart;*/
        int quantity;
        try {
            quantity = Integer.parseInt(partQuantity.getText().toString());
        } catch (NumberFormatException e) {
            quantity = 0;
        }
        mainActivity.partQuantity = quantity;
        super.onPause();
    }

    private void setInfoWithoutSavedInstanceState() {
/*        mStarted = mainActivity.mStarted;
        mInProgress = mainActivity.mInProgress;
        selectedEmployee = mainActivity.selectedEmployee;
        selectedProcess = mainActivity.selectedProcess;
        selectedMachine = mainActivity.selectedMachine;
        selectedPart = mainActivity.selectedPart;

        employeeService = mainActivity.employeeService;
        processService = mainActivity.processService;
        machineService = mainActivity.machineService;
        partService = mainActivity.partService;*/

        int quantity = mainActivity.partQuantity;
        if (quantity > 0) {
            partQuantity.setText(String.valueOf(quantity));
        }

        if (mainActivity.selectedEmployee != null) {
            employeeInfo.setText(mainActivity.selectedEmployee.getEmployeeName());
            employeeInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }

        if (mainActivity.selectedProcess != null) {
            processInfo.setText(mainActivity.selectedProcess.getProcessName());
            processInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }

        if (mainActivity.selectedMachine != null) {
            machineInfo.setText(mainActivity.selectedMachine.getMachineName());
            machineInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }

        if (mainActivity.selectedPart != null) {
            partInfo.setText(mainActivity.selectedPart.getPartName());
            partInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
    }

    /**
     * Setting saved data
     *//*
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
    }*/
/*    *//**
     * Save data before activity termination
     * @param outState
     *//*
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
    }*/

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
            mainActivity.selectedEmployee = mainActivity.employeeService.get(employeeId);
            employeeInfo.setText(mainActivity.selectedEmployee.getEmployeeName());
            employeeInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (data.hasExtra("processId")) {
            int processId = data.getExtras().getInt("processId");
            mainActivity.selectedProcess = mainActivity.processService.get(processId);
            processInfo.setText(mainActivity.selectedProcess.getProcessName());
            processInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (data.hasExtra("machineId")) {
            int machineId = data.getExtras().getInt("machineId");
            mainActivity.selectedMachine = mainActivity.machineService.get(machineId);
            machineInfo.setText(mainActivity.selectedMachine.getMachineName());
            machineInfo.setTextColor(getResources().getColor(R.color.result_exists_data));
        }
        if (data.hasExtra("partId")) {
            int partId = data.getExtras().getInt("partId");
            mainActivity.selectedPart = mainActivity.partService.get(partId);
            partInfo.setText(mainActivity.selectedPart.getPartName());
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

        if (mainActivity.mInProgress) {
            if (mainActivity.mStarted) {
                mHandler.sendEmptyMessage(MSG_UPDATE);
                startButton.setText(R.string.button_stop);
            } else {
                startButton.setText(R.string.button_resume);
            }
        }
    }
}

