package com.ahmedashraf.metroapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Station.class, countStation.class}, version = 1)
public abstract class StationDatabase extends RoomDatabase {
    public abstract StationDAO stationDAO();

    private static StationDatabase ourInstance;

    public static StationDatabase getDB(Context context) {
        if (ourInstance == null) {

                ourInstance = Room.databaseBuilder(context,
                        StationDatabase.class, "Stations")
                        .createFromAsset("Databases/Stations.db")
//                    .allowMainThreadQueries()
                        .build();
        }
        return ourInstance;
    }
}
