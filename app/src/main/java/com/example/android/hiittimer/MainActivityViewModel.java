package com.example.android.hiittimer;

import android.app.Application;
import android.view.View;

import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivityViewModel extends AndroidViewModel {

    private final Repository repository;
    private CompositeDisposable disposable;
    private NavigateLiveData<Object> mOpenEditDetail = new NavigateLiveData<>();
    private NavigateLiveData<View> mOpenTimerActivity = new NavigateLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
        this.disposable = new CompositeDisposable();
    }

    public LiveData<List<Asset>> startLocal() {
        Asset asset = new Asset();
        asset.setDefaultMyself();
        repository.saveAsset(asset);
        return repository.getAssets();
    }

    public NavigateLiveData<Object> getOpenDetailEditEvent(){return mOpenEditDetail;}

    public NavigateLiveData<View> getOpenTimerActivity(){return mOpenTimerActivity;}
}
