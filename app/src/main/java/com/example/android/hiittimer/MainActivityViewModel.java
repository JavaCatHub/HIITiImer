package com.example.android.hiittimer;

import android.app.Application;
import android.util.Log;

import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private final Repository repository;
    private CompositeDisposable disposable;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
        this.disposable = new CompositeDisposable();
    }

    public LiveData<List<Asset>> startLocal() {
        Asset asset = new Asset();
        asset.setTitle("Default");
        asset.setPrepare(1000);
        asset.setWorkOut(2000);
        asset.setInterval(1000);
        asset.setCoolDown(6000);
        asset.setCycle(8);
        asset.setSet(2);
        asset.setTotalTime(asset.calculateTotalTime());
        asset.setComment("This is the test");

        repository.saveAsset(asset);
        return repository.getAssets();
    }
}
