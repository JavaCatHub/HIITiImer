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

public class EditViewModel extends AndroidViewModel {
    private Repository repository;

    private boolean mIsNewAsset;

    private int mAssetId;

    private MutableLiveData<String> title = new MutableLiveData<>();

    private MutableLiveData<String> comment = new MutableLiveData<>();

    private NavigateLiveData<Void> save = new NavigateLiveData<>();

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

    private void updateAsset(Asset asset){repository.updateAsset(asset);}

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public MutableLiveData<String> getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment.setValue(comment);
    }
}
