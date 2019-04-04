package com.example.android.hiittimer;

import android.os.CountDownTimer;

public class CountDown extends CountDownTimer {
    private ClockInterface clockInterface;
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

    interface ClockInterface {
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
