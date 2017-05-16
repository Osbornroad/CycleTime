package com.gmail.osbornroad.cycletime;


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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopWatchFragment extends Fragment implements NavigationFragment {

    public StopWatchFragment() {
        // Required empty public constructor
    }

    private final int FRAGMENT_ID = 0;

    private final int MSG_START = 0;
    private final int MSG_RESUME = 2;
    private final int MSG_STOP = 3;
    private final int MSG_UPDATE = 4;

    private MainActivity mainActivity;

    /**
     * StopWatch is singletone
     */
    private StopWatch stopWatch;
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
    private TextView employeeInfo;
    private TextView processInfo;
    private TextView machineInfo;
    private TextView partInfo;
    private EditText partQuantity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        employeeInfo = (TextView) rootView.findViewById(R.id.employee_name_data);
        employeeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.switchFragment(EmployeesFragment.class, getResources().getString(R.string.employees_fragment_title));
/*                Intent intent = new Intent(v.getContext(), EmployeeChooseActivity.class);
                startActivityForResult(intent, 0);*/
            }
        });
        /**
         * Process setting interface creating
         */
        processInfo = (TextView) rootView.findViewById(R.id.process_name_data);
        processInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.switchFragment(ProcessesFragment.class, getResources().getString(R.string.processes_fragment_title));
/*                Intent intent = new Intent(v.getContext(), ProcessChooseActivity.class);
                startActivityForResult(intent, 0);*/
            }
        });
        /**
         * Machine setting interface creating
         */
        machineInfo = (TextView) rootView.findViewById(R.id.machine_name_data);
        machineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.switchFragment(MachinesFragment.class, getResources().getString(R.string.machines_fragment_title));
/*                Intent intent = new Intent(v.getContext(), MachineChooseActivity.class);
                startActivityForResult(intent, 0);*/
            }
        });
        /**
         * Part setting interface creating
         */
        partInfo = (TextView) rootView.findViewById(R.id.part_name_data);
        partInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.switchFragment(PartsFragment.class, getResources().getString(R.string.parts_fragment_title));
/*                Intent intent = new Intent(v.getContext(), PartChooseActivity.class);
                startActivityForResult(intent, 0);*/
            }
        });

        partQuantity = (EditText) rootView.findViewById(R.id.part_qty_data);
        partQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        mainActivity.partQuantity = Integer.parseInt(partQuantity.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(mainActivity.getApplicationContext(), "Please input correct quantity", Toast.LENGTH_SHORT).show();
                        partQuantity.setText("");
                    }
                }
            }
        });

        mainActivity = (MainActivity) getActivity();
        /**
         * Setting saved data
         */
        setInfoWithoutSavedInstanceState();
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
        int quantity;
        try {
            quantity = Integer.parseInt(partQuantity.getText().toString());
        } catch (NumberFormatException e) {
            quantity = 0;
        }
        mainActivity.partQuantity = quantity;
        super.onPause();
    }

    @Override
    public void onResume() {
        stopWatchResume();
        super.onResume();
    }

    private void setInfoWithoutSavedInstanceState() {
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
     * Setting data, which has been received from child activities:
     * EmployeeChooseActivity
     * ProcessChooseActivity
     * MachineChooseActivity
     */
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (data.hasExtra("employee")) {
            mainActivity.selectedEmployee = (Employee) data.getSerializableExtra("employee");
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
    }*/
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

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }
}

