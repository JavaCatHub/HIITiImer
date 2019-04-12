package com.example.android.hiittimer.repository.localdatasource;

import com.example.android.hiittimer.model.Asset;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LocalDataSource {
    private final AssetDAO mAssetDAO;
    private static final String TAG = LocalDataSource.class.getSimpleName();


    public LocalDataSource(AssetDAO assetDAO) {
        this.mAssetDAO = assetDAO;
    }

    public LiveData<List<Asset>> getAssets() {
        return mAssetDAO.getAssets();
    }

    public LiveData<Asset> getAsset(int id){return mAssetDAO.getAsset(id);}

    private void completable(Action action) {
        Completable.fromAction(action)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.tag(TAG).d("complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.tag(TAG).e(e);
                    }
                });
    }

    public void insertAsset(Asset asset) {
        completable(() -> mAssetDAO.insertAsset(asset));
    }

    public void updateAsset(Asset asset) {
        completable(() -> mAssetDAO.updateAsset(asset));
    }

    public void deleteAsset(Asset asset) {
        completable(() -> mAssetDAO.deleteAsset(asset));
    }
}
