package com.ahmedashraf.metroapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "countStations")
public class countStation {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public String Stations;

    public int Count;
}
