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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAsset(Asset asset);

    @Update
    void updateAsset(Asset asset);

    @Delete
    void deleteAsset(Asset asset);
}
