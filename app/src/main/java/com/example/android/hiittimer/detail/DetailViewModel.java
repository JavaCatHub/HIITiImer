package com.example.android.hiittimer.detail;

import android.app.Application;

import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DetailViewModel extends AndroidViewModel {

    private Repository repository;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        this.repository = new Repository(application);
    }

    public LiveData<Asset> start(int id){
        return repository.getAsset(id);
    }
}

