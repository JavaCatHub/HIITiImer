package com.example.android.hiittimer.repository.localdatasource;

import com.example.android.hiittimer.model.Asset;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AssetDAO {

    @Query("SELECT * FROM asset ORDER BY _id")
    LiveData<List<Asset>> getAssets();

    @Query("SELECT * FROM asset WHERE _id = :id")
    LiveData<Asset> getAsset(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAsset(Asset asset);

    @Update
    void updateAsset(Asset asset);

    @Query("DELETE FROM asset WHERE _id = :id")
    void deleteAsset(int id);
}
