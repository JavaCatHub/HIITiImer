package com.example.android.hiittimer;

import android.util.Pair;

import com.example.android.hiittimer.model.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import timber.log.Timber;

public class TimerViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private List<CountDown> timerList = new ArrayList<>();

    private final long interval = 1000;

    public TimerViewModelFactory(Asset asset){
        prepareCountDownSet(asset);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TimerViewModel(timerList);
    }


    private void prepareCountDownSet(Asset asset) {

        timerList.add(new CountDown(asset.getPrepare(), interval));
        for (int i = 0; i < asset.getSet(); i++) {
            for (int j = 0; j < asset.getCycle(); j++) {
                timerList.add(new CountDown(asset.getWorkOut(), interval));
                timerList.add(new CountDown(asset.getInterval(), interval));
            }
            timerList.add(new CountDown(asset.getCoolDown(),interval));
        }
        timerList.remove(timerList.size() - 1);
        Timber.d(timerList.toString());
    }
}
