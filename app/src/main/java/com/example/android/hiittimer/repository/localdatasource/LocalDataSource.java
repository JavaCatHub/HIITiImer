package com.example.android.hiittimer.repository.localdatasource;

import android.appwidget.AppWidgetManager;

import com.example.android.hiittimer.HIITWidgetProvider;
import com.example.android.hiittimer.model.Asset;

import java.util.List;
import java.util.concurrent.Callable;

import androidx.lifecycle.LiveData;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.ScalarCallable;
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

    public LiveData<Asset> getAsset(int id) {
        return mAssetDAO.getAsset(id);
    }

    public LiveData<Asset> getDefaultAsset() {
        return mAssetDAO.getDefaultAsset();
    }

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
                        Timber.i("complete");
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

    public void updateDefaultAsset(boolean status, int id) {
        Completable.fromAction(() -> {
            mAssetDAO.setTrueToFalse();
            Timber.i("onSetTrueToFalse");
        })
                .concatWith(new Completable() {
                    @Override
                    protected void subscribeActual(CompletableObserver observer) {
                        mAssetDAO.updateDefaultAsset(status, id);
                        Timber.i("onConcat");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.i("onSubscribed");
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void setTrueToFalse() {
        completable(mAssetDAO::setTrueToFalse);
    }

    public void deleteAsset(int id) {
        completable(() -> mAssetDAO.deleteAsset(id));
    }
}
