package com.example.android.hiittimer.repository;

import android.app.Application;

import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.localdatasource.AppDatabase;
import com.example.android.hiittimer.repository.localdatasource.LocalDataSource;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    private LocalDataSource mLocalDataSource;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.mLocalDataSource = new LocalDataSource(database.assetDAO());
    }

    public LiveData<List<Asset>> getAssets() {
        return mLocalDataSource.getAssets();
    }

    public LiveData<Asset> getAsset(int id) {
        return mLocalDataSource.getAsset(id);
    }

    public LiveData<Asset> getDefaultAsset() {
        return mLocalDataSource.getDefaultAsset();
    }

    public void insertAsset(Asset asset) {
        mLocalDataSource.insertAsset(asset);
    }

    public void updateAsset(Asset asset) {
        mLocalDataSource.updateAsset(asset);
    }

    public void updateDefaultAsset(boolean status, int id) {
        mLocalDataSource.updateDefaultAsset(status,id);
    }

    public void setTrueToFalse(){mLocalDataSource.setTrueToFalse();}

    public void deleteAsset(int id) {
        mLocalDataSource.deleteAsset(id);
    }
}

