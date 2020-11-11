package com.ahmedashraf.metroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Trips extends AppCompatActivity {
    ListView lvTrips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        lvTrips = findViewById(R.id.lvTrips);


        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        pool.execute(()-> {
            List<Station> trips = StationDatabase.getDB(this).stationDAO().getTrips();
            if(trips.size() > 0){
                ArrayList<String> trip = new ArrayList<>();
                trips.stream().forEach(t -> trip.add(t.StartS + " - " + t.EndS));
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, trip);
                runOnUiThread(()-> lvTrips.setAdapter(adapter));
            }
        });


        lvTrips.setOnItemClickListener((adapterView, view, i, l) -> {
            String trip = (String) lvTrips.getItemAtPosition(i);

            Intent in = new Intent();
            in.putExtra("trips", trip);
            setResult(RESULT_OK, in);
            finish();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void getTrips(MenuItem item) {
    }
    public void TripsCounts(MenuItem item) {
        Intent in = new Intent(this, countStations.class);
        startActivity(in);
    }

}