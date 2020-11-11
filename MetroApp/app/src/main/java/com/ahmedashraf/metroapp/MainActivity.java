package com.ahmedashraf.metroapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Spinner spnStartStation, spnEndStation;
    ArrayAdapter arrayAdapter;
    TextView tvResult;
    SharedPreferences sp;
    List<String> Line1 = Arrays.asList("Helwan", "Ain Helwan", "Helwan University", "Wadi Hof", "Hadayeq Helwan", "El-Maasara", "Tora El-Asmant", "Kozzika", "Tora El-Balad", "Thakanat El-Maadi", "Maadi", "Hadayeq El-Maadi", "Dar El-Salam", "ElZahraa", "Mar Girgis", "El-Malek El-Saleh", "AlSayyeda Zeinab", "Saad Zaghloul", "Sadat", "Gamal AbdAl Nasser", "Orabi", "Al Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-Sadr", "Kobri El-Qobba", "Hammamat El-Qobba", "Saray El-Qobba", "Hadayeq El-Zaitoun", "Helmeyet El-Zaitoun", "El-Matareyya", "Ain Shams", "Ezbet El-Nakhl", "El-Marg", "New El-Marg");
    List<String> Line2 = Arrays.asList("El Mounib", "Sakiat Mekki", "Omm el Misryeen", "Giza", "Faisal", "Cairo University", "Bohooth", "Dokki", "Opera", "Sadat", "Naguib", "Ataba", "Al Shohadaa", "Massara", "Road El-Farag", "Sainte Teresa", "Khalafawy", "Mezallat", "Koliet El-Zeraa", "Shobra El Kheima");
    List<String> Line3_1 = Arrays.asList("Airport", "Omar Ibn Al Khattab", "Ain Shams 2", "Ain Shams 1", "El Arab", "Alf Maskan", "Heliopolis Square", "Haroun", "Al Ahram", "Koleyet El Banat", "Cairo Stadium", "Cairo Fairground", "Abbassiya", "Fair Zone", "Abdou Pasha", "El Geish", "Bab El Shaaria", "Ataba", "Gamal AbdAl Nasser", "Maspero", "Zamalek", "Kit Kat", "Sudan St.", "Imbaba", "El Moneera", "El Bohy", "Al-Kawmeiah", "Ring Road", "Rod Al-Farag Corridor");
    List<String> Line3_2 = Arrays.asList("El Tawfikeya", "Wdi El Nil", "Gamaet Al-Dowal", "Bolak Al-Dakror", "Cairo University");
    List<String> Line3_3 = new ArrayList<>(Line3_2);

    ArrayList<Location> LocLine1 = new ArrayList<>();
    ArrayList<Location> LocLine2 = new ArrayList<>();
    ArrayList<Location> LocLine3_1 = new ArrayList<>();
    ArrayList<Location> LocLine3_2 = new ArrayList<>();
    ArrayList<Location> LocLine3_3 = new ArrayList<>();
    ArrayList<String> All;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spnStartStation = findViewById(R.id.spnStartStation);
        spnEndStation = findViewById(R.id.spnEndStation);
        tvResult = findViewById(R.id.tvResult);

        TreeSet myTreeSet = new TreeSet();
        myTreeSet.addAll(Line1);
        myTreeSet.addAll(Line2);
        myTreeSet.addAll(Line3_1);
        myTreeSet.addAll(Line3_2);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myTreeSet.toArray());
        spnStartStation.setAdapter(arrayAdapter);
        spnEndStation.setAdapter(arrayAdapter);

        All = new ArrayList<>(myTreeSet);
        sp = getPreferences(MODE_PRIVATE);
        String destination = sp.getString("destination", "");
        if (!destination.isEmpty()) {
            String[] stations = destination.split("\t");

            spnStartStation.setSelection(All.indexOf(stations[0]));
            spnEndStation.setSelection(All.indexOf(stations[1]));
        }

        Collections.reverse(Line3_3);
        Line3_3.addAll(Line3_1.subList(Line3_1.size() - 8, Line3_1.size() - 1));

        start(null);

//        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        pool.execute(()->{
////            LocLine1.addAll(getGeoLocation(Line1));
////            LocLine1.addAll(getGeoLocation(Line2));
////            LocLine1.addAll(getGeoLocation(Line3_1));
////            LocLine1.addAll(getGeoLocation(Line3_2()));
////            LocLine1.addAll(getGeoLocation(Line3_3));
////            Log.i("TAG", "done loading");
//            // could be save in shared pref in order not to be loaded every time
//        });


        Toast.makeText(this, "Please select your destination", Toast.LENGTH_LONG).show();
    }

     ArrayList<Location> getGeoLocation(List<String> Line){
        Location loc = new Location("");
        Geocoder geocoder = new Geocoder(this);
        ArrayList<Location> LocLine = new ArrayList<>();
        List<Address> address;
        for (int i = 0; i < Line.size(); i++) {
            LocLine.add(loc);
            try {
                address = geocoder.getFromLocationName(Line.get(i) , 1, 25.00,25.00,35.00,35.00);
                if(address.size() != 0){
                    LocLine.get(i).setLatitude(address.get(0).getLatitude());
                    LocLine.get(i).setLongitude(address.get(0).getLongitude());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return LocLine;
    }

    public void start(View view) {
        String StartStation = spnStartStation.getSelectedItem().toString();
        String EndStation = spnEndStation.getSelectedItem().toString();
        if (StartStation == EndStation) {
            if (view != null)
                YoYo.with(Techniques.Bounce).duration(500).repeat(2).playOn(view);
            return;
        }
        if (view != null)
            YoYo.with(Techniques.Pulse).duration(500).repeat(2).playOn(view);
        tvResult.setText("");
        byte NoOfStations1 = 0, NoOfStations2 = 0;

        if (Line1.contains(StartStation)) {
            if (Line1.contains(EndStation)) {
                Route(Line1, null, StartStation, EndStation, null);
            } else if (Line2.contains(EndStation)) {
                NoOfStations1 = Route(Line1, Line2, StartStation, EndStation, "Al Shohadaa");
                tvResult.append("\n");
                NoOfStations2 = Route(Line1, Line2, StartStation, EndStation, "Sadat");
            } else if (Line3_2.contains(EndStation)) {
                NoOfStations1 = Route(Line1, Line3_2(), StartStation, EndStation, "Gamal AbdAl Nasser");
            } else if (Line3_1.contains(EndStation)) {
                NoOfStations1 = Route(Line1, Line3_1, StartStation, EndStation, "Gamal AbdAl Nasser");
            }
        } else if (Line2.contains(StartStation)) {
            if (Line2.contains(EndStation)) {
                Route(Line2, null, StartStation, EndStation, null);
            } else if (Line1.contains(EndStation)) {
                NoOfStations1 = Route(Line2, Line1, StartStation, EndStation, "Al Shohadaa");
                tvResult.append("\n");
                NoOfStations2 = Route(Line2, Line1, StartStation, EndStation, "Sadat");
            } else if (Line3_3.contains(EndStation)) {
                NoOfStations1 = Route(Line2, Line3_3, StartStation, EndStation, "Cairo University");
            } else if (Line3_2.contains(EndStation)) {
                NoOfStations1 = Route(Line2, Line3_2(), StartStation, EndStation, "Ataba");
                tvResult.append("\n");
                NoOfStations2 = Route(Line2, Line3_2(), StartStation, EndStation, "Cairo University");
            } else if (Line3_1.contains(EndStation)) {
                NoOfStations1 = Route(Line2, Line3_1, StartStation, EndStation, "Ataba");
            }
        } else if (Line3_3.contains(StartStation)) {
            if (Line3_3.contains(EndStation)) {
                Route(Line3_3, null, StartStation, EndStation, null);
            } else if (Line2.contains(EndStation)) {
                NoOfStations1 = Route(Line3_3, Line2, StartStation, EndStation, "Cairo University");
            }
        } else if (Line3_2.contains(StartStation)) {
            if (Line3_2.contains(EndStation)) {
                Route(Line3_2(), null, StartStation, EndStation, null);
            } else if (Line1.contains(EndStation)) {
                NoOfStations1 = Route(Line3_2(), Line1, StartStation, EndStation, "Gamal AbdAl Nasser");
            } else if (Line2.contains(EndStation)) {
                NoOfStations1 = Route(Line3_2(), Line2, StartStation, EndStation, "Ataba");
                tvResult.append("\n");
                NoOfStations2 = Route(Line3_2(), Line2, StartStation, EndStation, "Cairo University");
            } else if (Line3_1.contains(EndStation)) {
                NoOfStations1 = Route(Line3_2, Line3_1.subList(0, Line3_1.indexOf("Sudan St.")), StartStation, EndStation, "Kit Kat");
            }

        } else if (Line3_1.contains(StartStation)) {
            if (Line3_1.contains(EndStation)) {
                Route(Line3_1, null, StartStation, EndStation, null);
            } else if (Line1.contains(EndStation)) {
                NoOfStations1 = Route(Line3_1, Line1, StartStation, EndStation, "Gamal AbdAl Nasser");
            } else if (Line2.contains(EndStation)) {
                NoOfStations1 = Route(Line3_1, Line2, StartStation, EndStation, "Ataba");
            } else if (Line3_2.contains(EndStation)) {
                NoOfStations1 = Route(Line3_1.subList(0, Line3_1.indexOf("Sudan St.")), Line3_2, StartStation, EndStation, "Kit Kat");
            }
        } else
            Toast.makeText(this, "Your station isn't listed", Toast.LENGTH_SHORT).show();

        if (NoOfStations2 != 0) {
            if (NoOfStations1 <= NoOfStations2) {
                tvResult.append("Shortest route is the First Route\n");
            } else {
                tvResult.append("Shortest route is the Second Route\n");
            }
        }

        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        pool.execute(()->{
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("destination", StartStation + "\t" + EndStation);
            editor.commit();

            if (view != null){
                StationDatabase.getDB(this).stationDAO().insert(StartStation, EndStation);
            }
        });
    }

    public Byte Route(List<String> line1, List<String> line2, String startStation, String endStation, String Intersect) {
        ArrayList<String> route1 = new ArrayList<>();
        byte NoOfStations, EndIndex, distance1;
        byte intersectIndex1 = (byte) line1.indexOf(Intersect);
        byte StartIndex = (byte) line1.indexOf(startStation);

        if (Intersect == null) {
            EndIndex = (byte) line1.indexOf(endStation);
            distance1 = (byte) (EndIndex - StartIndex);
            if (distance1 > 0) {
                route1.addAll(line1.subList(StartIndex + 1, EndIndex + 1));
            } else {
                route1.addAll(line1.subList(EndIndex, StartIndex));
                Collections.reverse(route1);
            }
            NoOfStations = (byte) Math.abs(distance1);
        } else {
            EndIndex = (byte) line2.indexOf(endStation);
            ArrayList<String> route2 = new ArrayList<>();
            byte intersectIndex2 = (byte) line2.indexOf(Intersect);
            distance1 = (byte) (intersectIndex1 - StartIndex);
            byte distance2 = (byte) (EndIndex - intersectIndex2);

            if (distance1 > 0) {
                route1.addAll(line1.subList(StartIndex + 1, intersectIndex1 + 1));
            } else {
                route1.addAll(line1.subList(intersectIndex1, StartIndex));
                Collections.reverse(route1);
            }

            if (distance2 > 0) {
                route2.addAll(line2.subList(intersectIndex2 + 1, EndIndex + 1));
            } else {
                route2.addAll(line2.subList(EndIndex, intersectIndex2));
                Collections.reverse(route2);
            }
            route1.addAll(route2);

            NoOfStations = (byte) (Math.abs(distance1) + Math.abs(distance2));
        }


        tvResult.append("No. of Stations : " + NoOfStations + " Station\n");

        tvResult.append("Estimated time is : " + (NoOfStations * 2) + " minutes\n\n");

        tvResult.append("ٌRoute: " + route1 + "\n\n");

        if (NoOfStations < 9) {
            tvResult.append("Ticket price is 5 L.E\n");
        } else if (NoOfStations < 16) {
            tvResult.append("Ticket price is 7 L.E\n");
        } else {
            tvResult.append("Ticket price is 10 L.E\n");
        }
        return NoOfStations;
    }

    public List<String> Line3_2() {
        ArrayList<String> line3 = new ArrayList<>(Line3_1.subList(0, 21)); // 21 =  Kitkat
        line3.addAll(Line3_2);
        return line3;
    }

    public void map(View view) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void LocateStart(View view) {
        String station = spnStartStation.getSelectedItem().toString();

        if (getLocation(station, Line1, LocLine1)) {

        } else if (getLocation(station, Line2, LocLine2)) {

        } else if (getLocation(station, Line3_1, LocLine3_1)) {

        } else if (getLocation(station, Line3_2(), LocLine3_2)) {

        } else if (getLocation(station, Line3_3, LocLine3_3)) {

        }
    }

    public void LocateEnd(View view) {
        String station = spnEndStation.getSelectedItem().toString();

        if (getLocation(station, Line1, LocLine1)) {

        } else if (getLocation(station, Line2, LocLine2)) {

        } else if (getLocation(station, Line3_1, LocLine3_1)) {

        } else if (getLocation(station, Line3_2(), LocLine3_2)) {

        } else if (getLocation(station, Line3_3, LocLine3_3)) {

        }
    }

    public boolean getLocation(String station, List<String> Line, ArrayList<Location> locLine) {
        if (Line.contains(station)) {
            double latitude, longitude;
            byte index = (byte) Line.indexOf(station);
            try {
                latitude = locLine.get(index).getLatitude();
                longitude = locLine.get(index).getLongitude();
                Log.i("TAG", " " + latitude + " " + longitude);
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude));
                startActivity(in);
            }
            catch (Exception e){
                Toast.makeText(this, "Can't locate this station", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }


    LocationManager Manager;
    public void Nearest(View view) {
        Manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perm, 1);
        } else {
            Manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    Manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull final Location location1) {
        float distance=10000, distance1;
        byte index = 0;
        for (int i = 0; i < LocLine1.size(); i++) {
            distance1 = location1.distanceTo(LocLine1.get(i));
            if (distance > distance1) {
                distance = distance1;
                index = (byte) i;
            }
        }

        for (int i = 0; i < LocLine2.size(); i++) {
            distance1 = location1.distanceTo(LocLine2.get(i));
            if (distance > distance1) {
                distance = distance1;
                index = (byte) i;
            }

        }
        for (int i = 0; i < LocLine3_1.size(); i++) {
            distance1 = location1.distanceTo(LocLine3_1.get(i));
            if (distance > distance1) {
                distance = distance1;
                index = (byte) i;
            }
        }
        for (int i = 0; i < LocLine3_2.size(); i++) {
            distance1 = location1.distanceTo(LocLine3_2.get(i));
            if (distance > distance1) {
                distance = distance1;
                index = (byte) i;
            }
        }

        for (int i = 0; i < LocLine3_3.size(); i++) {
            distance1 = location1.distanceTo(LocLine3_3.get(i));
            if (distance > distance1) {
                distance = distance1;
                index = (byte) i;
            }
        }

        spnStartStation.setSelection(index);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK) {
                String[] trip = data.getStringExtra("trips").split(" - ");
                spnStartStation.setSelection(All.indexOf(trip[0]));
                spnEndStation.setSelection(All.indexOf(trip[1]));
                start(null);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void TripsCounts(MenuItem item) {
        Intent in = new Intent(this, countStations.class);
        startActivity(in);
    }
}