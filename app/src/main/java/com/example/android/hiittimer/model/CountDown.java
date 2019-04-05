package com.example.android.hiittimer.model;

import android.os.CountDownTimer;

public class CountDown extends CountDownTimer {
    private ClockInterface clockInterface;

    private String title;

    private int set;

    private int cycle;

    public int getSet() {
        return set;
    }

    public int getCycle() {
        return cycle;
    }

    public String getTitle() {
        return title;
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public CountDown(long millisInFuture, long countDownInterval, String title, int set, int cycle){
        super(millisInFuture,countDownInterval);
        this.title = title;
        this.set = set;
        this.cycle = cycle;
    }

    public interface ClockInterface {
        void onTickTextView(long millisUntilFinished);

        void onFinishTextView();
    }

    public void setClockInterface(ClockInterface clockInterface) {
        this.clockInterface = clockInterface;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        clockInterface.onTickTextView(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        clockInterface.onFinishTextView();
    }
}
