package com.gmail.osbornroad.cycletime;

/**
 * Created by User on 24.04.2017.
 */

public class StopWatch {

    private long mStartTime = 0;
    private long mStopTime = 0;
    private long mTotalPaused = 0;
    private boolean mRunning = false;

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

//    public void onPause() {
//        mStopTime = System.currentTimeMillis();
//        mRunning = false;
//    }

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


/*    public static void main(String[] args) throws InterruptedException {
        final StopWatch s = new StopWatch();
        s.onStart();
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    System.out.println(s.getFormattedElapsedTime());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
        Thread.sleep(5000);
        s.onPause();
        Thread.sleep(5000);
        s.onResume();
        Thread.sleep(5000);
        s.onPause();
        Thread.sleep(5000);
        s.onResume();
        Thread.sleep(5000);
        s.onStop();
    }*/
}
