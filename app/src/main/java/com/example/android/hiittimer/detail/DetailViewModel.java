package com.example.android.hiittimer.detail;

import android.app.Application;

import com.example.android.hiittimer.NavigateLiveData;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DetailViewModel extends AndroidViewModel {

    private Repository repository;

    public int getAssetId() {
        return mAssetId;
    }

    private Asset asset;

    private int mAssetId;

    private NavigateLiveData<Void> mEditAssetEvent = new NavigateLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public NavigateLiveData<Void> getEditAssetEvent() {
        return mEditAssetEvent;
    }

    public LiveData<Asset> start(int  id){
        mAssetId = id;
        return repository.getAsset(id);
    }

    public void delete(){
        repository.deleteAsset(mAssetId);
    }

    public void updateDefaultAsset(boolean status, int id){
        repository.updateDefaultAsset(status,id);
    }

    public void setTrueToFalse(){
        repository.setTrueToFalse();
    }
}

