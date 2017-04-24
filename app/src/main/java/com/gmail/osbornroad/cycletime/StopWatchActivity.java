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
//    final int MSG_PAUSE = 1;
    private final int MSG_RESUME = 2;
    private final int MSG_STOP = 3;
    private final int MSG_UPDATE = 4;


    private StopWatch stopWatch = new StopWatch();
    private final int REFRESH_RATE = 100;

    private TextView timeRunningView;
    private Button startButton;
//    Button pauseButton;
    private Button resumeButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        timeRunningView = (TextView) findViewById(R.id.time_running_view);
        startButton = (Button) findViewById(R.id.start_button);
//        pauseButton = (Button) findViewById(R.id.pause_button);
        resumeButton = (Button) findViewById(R.id.resume_button);
        stopButton = (Button) findViewById(R.id.stop_button);
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
/*                case MSG_PAUSE:
                    mHandler.removeMessages(MSG_UPDATE);
                    stopWatch.onPause();
                    timeRunningView.setText(stopWatch.getFormattedElapsedTime());
                    break;*/
                case MSG_RESUME:
                    stopWatch.onResume();
                    mHandler.sendEmptyMessage(MSG_UPDATE);
                    break;
                default:
                    break;
            }
        }
    };

    public void onClickStart(View view) {
        mHandler.sendEmptyMessage(MSG_START);
    }

/*    public void onClickPause(View view) {
        mHandler.sendEmptyMessage(MSG_PAUSE);
    }*/

    public void onClickResume(View view) {
        mHandler.sendEmptyMessage(MSG_RESUME);
    }

    public void onClickStop(View view) {
        mHandler.sendEmptyMessage(MSG_STOP);
    }
}
