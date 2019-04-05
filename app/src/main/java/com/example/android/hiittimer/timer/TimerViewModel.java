package com.example.android.hiittimer.timer;

import com.example.android.hiittimer.model.CountDown;

import java.util.Iterator;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {

    private MutableLiveData<CountDown> countDown = new MutableLiveData<>();

    private Iterator<CountDown> iterator;

    public TimerViewModel(List<CountDown> timerList) {
        this.iterator = timerList.iterator();
    }

    public MutableLiveData<CountDown> getCountDown() {
        return countDown;
    }

    public void setCountDown(CountDown countDown) {
        this.countDown.setValue(countDown);
    }

    public MutableLiveData<CountDown> startCountDown() {
        nextCountdown();
        return getCountDown();
    }

    boolean nextCountdown(){
        if(iterator.hasNext()){
            setCountDown(iterator.next());
            return true;
        }
        return false;
    }
}
