package com.example.android.hiittimer.ui.edit;

import android.app.Application;
import android.view.View;

import com.example.android.hiittimer.NavigateLiveData;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EditViewModel extends AndroidViewModel {
    private Repository repository;

    private boolean mIsNewAsset;

    private int mAssetId;

    private NavigateLiveData<View> insert = new NavigateLiveData<>();

    public EditViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void setIsNewAsset(boolean mIsNewAsset) {
        this.mIsNewAsset = mIsNewAsset;
    }

    public LiveData<Asset> start() {
        return repository.getAsset(mAssetId);
    }

    public void saveAsset(Asset asset) {
        repository.saveAsset(asset);
    }

    public NavigateLiveData<View> getInsertLiveData() {
        return insert;
    }

    public void setAssetId(int assetId) {
        mAssetId = assetId;
    }
}
