package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.FakeEmployeeDaoImpl;
import com.gmail.osbornroad.cycletime.model.Employee;
import com.gmail.osbornroad.cycletime.service.EmployeeService;
import com.gmail.osbornroad.cycletime.service.EmployeeServiceImpl;

public class StopWatchActivity extends AppCompatActivity {

    private final int MSG_START = 0;
    private final int MSG_RESUME = 2;
    private final int MSG_STOP = 3;
    private final int MSG_UPDATE = 4;


    private StopWatch stopWatch;
    /** mStarted
    after Start button
    Including Pause condition
     */
    private boolean mStarted;
    /** mInProgress
    after Resume/Start button
    Continue counting
     */
    private boolean mInProgress;
    private final int REFRESH_RATE = 100;

    private TextView timeRunningView;
    private Button startButton;
    private Button resetButton;

    private EmployeeService employeeService;

    //Choosing employee
    private LinearLayout lineEmployee;
    private TextView employeeInfo;
    private Employee selectedEmployee;

/*    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView employeeRecyclerView;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        stopWatch  = StopWatch.getStopWatch();

        if (savedInstanceState != null && savedInstanceState.containsKey("mStarted")) {
            mStarted = savedInstanceState.getBoolean("mStarted");
        } else {
            mStarted = false;
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("mInProgress")) {
            mInProgress = savedInstanceState.getBoolean("mInProgress");
        } else {
            mInProgress = false;
        }

        employeeService = new EmployeeServiceImpl(new FakeEmployeeDaoImpl());

        timeRunningView = (TextView) findViewById(R.id.time_running_view);
        startButton = (Button) findViewById(R.id.start_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        lineEmployee = (LinearLayout) findViewById(R.id.line_employee);
        employeeInfo = (TextView) findViewById(R.id.employee_name_data);

        employeeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EmployeeChooseActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });
        setSampleInfo(savedInstanceState);
        stopWatchResume();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mStarted", mStarted);
        outState.putBoolean("mInProgress", mInProgress);
        if (selectedEmployee != null) {
            outState.putInt("employeeId", selectedEmployee.getId());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (data.hasExtra("employeeId")) {
            int employeeId = data.getExtras().getInt("employeeId");
            selectedEmployee = employeeService.get(employeeId);
            employeeInfo.setText(selectedEmployee.getEmployeeName());
        }
    }

    private void setSampleInfo(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("mStarted")) {
                mStarted = savedInstanceState.getBoolean("mStarted");
            }
            if (savedInstanceState.containsKey("mInProgress")) {
                mInProgress = savedInstanceState.getBoolean("mInProgress");
            }
            stopWatchResume();
            if (savedInstanceState.containsKey("employeeId")) {
                int id = savedInstanceState.getInt("employeeId");
                selectedEmployee = employeeService.get(id);
                employeeInfo.setText(selectedEmployee.getEmployeeName());
            }
        }
    }


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


    private void stopWatchResume() {
        if (mStarted) {
            if (mInProgress) {
                mHandler.sendEmptyMessage(MSG_UPDATE);
                startButton.setText(R.string.button_stop);
            } else {
                startButton.setText(R.string.button_resume);
            }
        }
    }

    public void onClickStart(View view) {
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

    public void onClickReset(View view) {
        if (!mStarted) {
            mInProgress = false;
            timeRunningView.setText("00:00:00");
            startButton.setText(R.string.button_start);
        }
    }
}
