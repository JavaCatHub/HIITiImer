package com.example.android.hiittimer.ui.edit;

import android.app.Application;
import android.view.View;

import com.example.android.hiittimer.NavigateLiveData;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.repository.Repository;

import androidx.annotation.NonNull;
import androidx.databinding.InverseMethod;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EditViewModel extends AndroidViewModel {
    private Repository repository;

    private boolean mIsNewAsset;

    private int mAssetId;

    private MutableLiveData<Asset> asset = new MutableLiveData<>();

    private NavigateLiveData<Void> save = new NavigateLiveData<>();

    public EditViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void setIsNewAsset(boolean mIsNewAsset) {
        this.mIsNewAsset = mIsNewAsset;
    }

    public LiveData<Asset> start(){
     return repository.getAsset(mAssetId);
    }

    public LiveData<Asset> startNewAsset(){
        Asset defaultAsset = new Asset();
        defaultAsset.setDefaultMyself();
        setAsset(defaultAsset);
        return asset;
    }

    public void saveAsset(Asset asset){
        if (mIsNewAsset){
            insertAsset(asset);
        }else{
            asset.setId(mAssetId);
            updateAsset(asset);
        }
    }

    private void insertAsset(Asset asset) {
        repository.insertAsset(asset);
    }

    public NavigateLiveData<Void> getSaveLiveData() {
        return save;
    }

    public void setAssetId(int assetId) {
        mAssetId = assetId;
    }

    public void updateAsset(Asset asset){repository.updateAsset(asset);}

    public MutableLiveData<Asset> getAsset(){ return asset;}

    public void setAsset(Asset asset){ this.asset.setValue(asset);}
}
