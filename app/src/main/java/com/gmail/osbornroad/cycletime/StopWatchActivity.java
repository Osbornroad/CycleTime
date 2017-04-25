package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.osbornroad.cycletime.model.Employee;

public class StopWatchActivity extends AppCompatActivity {

    private final int MSG_START = 0;
    private final int MSG_RESUME = 2;
    private final int MSG_STOP = 3;
    private final int MSG_UPDATE = 4;


    private StopWatch stopWatch = new StopWatch();
    private final int REFRESH_RATE = 100;

    private TextView timeRunningView;
    private Button startButton;
    private Button resetButton;

    //Choosing employee
    private LinearLayout lineEmployee;
    private TextView employeeInfo;
    private Employee selectedEmployee = null;

    private EmployeeListAdapter employeeListAdapter;
    private RecyclerView employeeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        timeRunningView = (TextView) findViewById(R.id.time_running_view);
        startButton = (Button) findViewById(R.id.start_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        lineEmployee = (LinearLayout) findViewById(R.id.line_employee);
        employeeInfo = (TextView) findViewById(R.id.employee_name_data);

        employeeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeInfo.setText("Hello");
                Toast.makeText(v.getContext(), "Employee choosen", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), EmployeeChooseActivity.class);
                startActivity(intent);
            }
        });
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

    private boolean mStarted = false;
    private boolean mInProgress = false;

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
