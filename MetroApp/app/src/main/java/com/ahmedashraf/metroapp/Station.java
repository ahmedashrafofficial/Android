package com.ahmedashraf.metroapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Trips")
public class Station {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public String StartS,EndS;
}
