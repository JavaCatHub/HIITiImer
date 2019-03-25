package com.example.android.hiittimer.repository.localdatasource;

import android.content.Context;

import com.example.android.hiittimer.model.Asset;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import timber.log.Timber;

@Database(entities = Asset.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "assets";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Timber.d(TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,DATABASE_NAME)
                        .build();
            }
        }
        Timber.d(TAG,"Getting the database instance");
        return sInstance;
    }
    public abstract AssetDAO assetDAO();
}
