package com.example.android.hiittimer.main;

import android.app.Application;
import android.view.View;

import com.example.android.hiittimer.NavigateLiveData;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivityViewModel extends AndroidViewModel {

    private final Repository repository;
    private CompositeDisposable disposable;
    private Asset mAsset;
    private NavigateLiveData<Asset> mOpenDetailEvent = new NavigateLiveData<>();
    private NavigateLiveData<Void> mOpenEditEvent = new NavigateLiveData<>();
    private NavigateLiveData<View> mOpenTimerActivity = new NavigateLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
        this.disposable = new CompositeDisposable();
    }

    public LiveData<List<Asset>> startLocal() {
        return repository.getAssets();
    }

    public LiveData<List<Asset>> getAssetList(){return repository.getAssets();}

    public LiveData<Asset> getDefaultAsset(){
        return repository.getDefaultAsset();
    }

    public void updateDefaultAsset(boolean status, int id){
        repository.updateDefaultAsset(status,id);
    }

    public Asset getAsset() {
        return mAsset;
    }

    public void setAsset(Asset asset) {
        this.mAsset = asset;
    }

    public NavigateLiveData<Asset> getOpenDetailEvent() {
        return mOpenDetailEvent;
    }

    public NavigateLiveData<View> getOpenTimerActivity() {
        return mOpenTimerActivity;
    }

    public NavigateLiveData<Void> getNewAssetEvent() {
        return mOpenEditEvent;
    }

    public void addNewAsset() {
        mOpenEditEvent.call();
    }
}
