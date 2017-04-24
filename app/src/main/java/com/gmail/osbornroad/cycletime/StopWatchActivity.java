package com.gmail.osbornroad.cycletime;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        timeRunningView = (TextView) findViewById(R.id.time_running_view);
        startButton = (Button) findViewById(R.id.start_button);
        resetButton = (Button) findViewById(R.id.reset_button);
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
