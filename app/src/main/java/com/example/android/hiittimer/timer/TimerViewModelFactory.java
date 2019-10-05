package com.example.android.hiittimer.timer;

import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.model.CountDown;
import com.example.android.hiittimer.timer.TimerViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import timber.log.Timber;

public class TimerViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private List<CountDown> timerList = new ArrayList<>();

    private Asset asset;

    private final long interval = 1000;

    public TimerViewModelFactory(Asset asset) {
        this.asset = asset;
        prepareCountDownSet(asset);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TimerViewModel(timerList, asset);
    }


    private void prepareCountDownSet(Asset asset) {
        timerList.add(
                new CountDown(asset.getPrepare(), interval, "Prepare", asset.getSet(), asset.getCycle())
        );

        /*
         i = set
         j = cycle
         */
        for (int i = asset.getSet(); i > 0; i--) {
            for (int j = asset.getCycle(); j > 0; j--) {
                timerList.add(new CountDown(asset.getWorkOut(), interval, "Workout", i, j));
                timerList.add(new CountDown(asset.getInterval(), interval, "Interval", i, j));
            }
            timerList.remove(timerList.size() - 1);
            timerList.add(new CountDown(asset.getCoolDown(), interval, "Cool down", i, 0));
        }
        timerList.remove(timerList.size() - 1);
        Timber.i(timerList.toString());
    }
}
