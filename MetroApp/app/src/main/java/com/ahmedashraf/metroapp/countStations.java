package com.ahmedashraf.metroapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class countStations extends AppCompatActivity {
    ListView lvCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_stations);
        lvCount = findViewById(R.id.lvCount);

        DBCount();
        //AndroidCount();

    }

    public void DBCount() {
        MyTask task = new MyTask();
        task.execute();
    }

    List<String> trips;
    ArrayList<String> station;
    ArrayList<Integer> count;
    public void AndroidCount(){
        station = new ArrayList<>();
        count = new ArrayList<>();

        trips = StationDatabase.getDB(this).stationDAO().getStart();
        Count();
        trips = StationDatabase.getDB(this).stationDAO().getEnd();
        Count();

        for (int i = 0; i < station.size(); i++) {
            trips.add(station.get(i) + " - " + count.get(i));
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, trips);
        lvCount.setAdapter(adapter);
        station.clear();
        count.clear();
    }

    public void Count(){
        byte index;
        if (trips.size() > 0) {
            for (int i = 0; i < trips.size(); i++) {
                if (trips.get(i) != "") {
                    if (station.contains(trips.get(i))) {
                        index = (byte) station.indexOf(trips.get(i));
                        byte frequency = (byte) (Collections.frequency(trips, trips.get(i)) + count.get(index));
                        count.set(index, (int) frequency);
                    } else {
                        station.add(trips.get(i));
                        count.add(Collections.frequency(trips, trips.get(i)));
                    }
                    Collections.replaceAll(trips, trips.get(i), "");
                }
            }
        }
        trips.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void getTrips(MenuItem item) {
        Intent in = new Intent(this, Trips.class);
        startActivityForResult(in, 1);
    }
    public void TripsCounts(MenuItem item) {
    }

    class MyTask extends AsyncTask<Void,Void,List<Counts>>{

        @Override
        protected List<Counts> doInBackground(Void... voids) {
            StationDatabase.getDB(countStations.this).stationDAO().DeleteCount();
            StationDatabase.getDB(countStations.this).stationDAO().insertCount();
            StationDatabase.getDB(countStations.this).stationDAO().insertCount2();
            List<Counts> counts = StationDatabase.getDB(countStations.this).stationDAO().CountStation();
            return counts;
        }

        @Override
        protected void onPostExecute(List<Counts> counts) {
            super.onPostExecute(counts);
            if (counts.size() > 0) {
                ArrayList<String> trip = new ArrayList<>();
                counts.stream().forEach(t -> trip.add(t.Stations + " - " + t.Count));
                ArrayAdapter adapter = new ArrayAdapter(countStations.this, android.R.layout.simple_list_item_1, trip);
                lvCount.setAdapter(adapter);
            }
        }
    }
}