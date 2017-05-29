package com.gmail.osbornroad.cycletime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 24.04.2017.
 */

public class StopWatch {

    private static StopWatch stopWatch;
    private long mStartTime = 0;
    private long mStopTime = 0;
    private long mTotalPaused = 0;
    private boolean mRunning = false;

    private StopWatch() {
    }

    public static StopWatch getStopWatch() {
        if (stopWatch == null) {
            stopWatch = new StopWatch();
        }
        return stopWatch;
    }

    public void onStart() {
        if (!mRunning) {
            mStartTime = System.currentTimeMillis();
            mTotalPaused = 0;
            mRunning = true;
        }
    }

    public void onStop() {
        if (mRunning) {
            mStopTime = System.currentTimeMillis();
            mRunning = false;
        }
    }

    public void onResume() {
        if (!mRunning) {
            mTotalPaused += System.currentTimeMillis() - mStopTime;
            mStopTime = 0;
            mRunning = true;
        }
    }

    public long getElapsedTime() {
        if (mRunning) {
            return System.currentTimeMillis() - mStartTime - mTotalPaused;
        }
        return mStopTime - mStartTime - mTotalPaused;
    }

    public long getElapsedTimeInSec() {
        return getElapsedTime() / 1000;
    }

    public String getFormattedElapsedTime() {
        int hours = (int) (getElapsedTimeInSec() / 3600);
        int minutes = (int) ((getElapsedTimeInSec() % 3600) / 60);
        int seconds = (int) (getElapsedTimeInSec() % 60);
        String output = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return output;
    }

    public String getStartDateTime() {
        String startDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(mStartTime));
        return startDateTime;
    }

}
