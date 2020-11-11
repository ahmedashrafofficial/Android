package com.ahmedashraf.metroapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface StationDAO {
    @Query("Insert into Trips (StartS,EndS) Values (:Start, :End) ")
    public void insert(String Start, String End);

    @Query("Select * from Trips")
    List<Station> getTrips();

    @Query("Select StartS from Trips")
    List<String> getStart();

    @Query("Select EndS from Trips")
    List<String> getEnd();

    @Query("Delete FROM countStations")
    public void DeleteCount();

    @Query("Insert into countStations (Stations, Count) select StartS, COUNT(*) FROM Trips GROUP BY StartS")
    public void insertCount();

    @Query("Insert into countStations (Stations, Count) select EndS, COUNT(*) FROM Trips GROUP BY EndS")
    public void insertCount2();

    @Query("SELECT Stations, Sum(Count) as Count FROM countStations GROUP BY Stations")
    List<Counts> CountStation();



}
