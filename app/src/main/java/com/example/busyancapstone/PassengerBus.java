package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Adapter.CustomAdapterBus;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Enum.NotificationTypes;
import com.example.busyancapstone.Enum.ReservationStatus;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Manager.MapsManager;
import com.example.busyancapstone.Model.LiveBus;
import com.example.busyancapstone.Model.LiveDrivers;
import com.example.busyancapstone.Model.LivePassengers;
import com.example.busyancapstone.Model.Notifications;
import com.example.busyancapstone.Model.SeatReservation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PassengerBus extends AppCompatActivity implements OnMapReadyCallback {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final long UPDATE_INTERVAL = 5000;
    private static final String TAG = "TAGPassengerBus";

    private BottomNavigationView bottomNavigationView;
    private TextView tv_destination;
    private static TextView tvNoBus;
    private ProgressBar progressBar;

    private Handler handler = new Handler();

    private boolean isPassengerUpdatesRunning = false;
    private double lattitude = 0.0, longitude = 0.0;
    private double destinationLat = 0.0, destinationLong = 0.0;
    private final String My_USER_ID = FirebaseManager.getMyUserId();
    private static String apiKey;
    private String address;
    private static LatLng myLatLng;
    private ListView busListView;

    private SupportMapFragment mapFragment;
    private LocationRequest locationRequest;
    private static GoogleMap mMap;
    private FusedLocationProviderClient client;
    private BusAsyncTask busAsyncTask;
    private static CustomAdapterBus adapterBus;
    private static SeatReservation reservation = SeatReservation.getInstance();

    private static final ArrayList<LiveDrivers> arrLiveDrivers = new ArrayList<>();
    private static final ArrayList<LiveBus> arrLiveBus = new ArrayList<>();
    private static final ArrayList<Marker> busMarkers = new ArrayList<>();
    private static final ArrayList<String> arrBusCodes = new ArrayList<>();

    private DatabaseReference liveDriversDb, livePassengerDb, seatReservationDb, notificationDb,
        busRoutesDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_bus);


        notificationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.NOTIFICATIONS.getValue());
        seatReservationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESERVATIONS.getValue());
        liveDriversDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.LIVE_DRIVERS.getValue());
        livePassengerDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.LIVE_PASSENGERS.getValue());
        busRoutesDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BUS_ROUTES.getValue());

        setLocationRequest();
        references();
        clickActions();

        adapterBus = new CustomAdapterBus(this, arrLiveBus, myLatLng, apiKey);
        busListView.setAdapter(adapterBus);
        adapterBus.notifyDataSetChanged();
    }

    private void setLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // Set interval to 10 seconds
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (mMap != null) {
                Location location = locationResult.getLastLocation();
                lattitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng latLng = new LatLng(lattitude, longitude);
                updateLocationInDb();
            }
        }
    };

    Runnable busUpdateRunnable = new Runnable() {
        @Override
        public void run() {

            Log.d(TAG, "run: busUpdateRunnable");
                liveDriversDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!isPassengerUpdatesRunning) return;

                        arrLiveDrivers.clear();
                        arrLiveBus.clear();

                        if (snapshot.hasChildren()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                LiveDrivers liveDrivers = dataSnapshot.getValue(LiveDrivers.class);
                                LatLng busLatLng = new LatLng(liveDrivers.getLattitude(), liveDrivers.getLongitude());

                                String busDriverId = liveDrivers.getBusDriverId();
                                String route = liveDrivers.getRoute();
                                String busCode = liveDrivers.getBusCode();
                                String plateNumber = liveDrivers.getPlateNumber();
                                String distance = MapsManager.calculateDistance(myLatLng, busLatLng);
                                String travelTime = MapsManager.estimateTravelTime(myLatLng, busLatLng, apiKey);

                                LiveBus liveBus = new LiveBus(
                                        busDriverId,
                                        route,
                                        busCode,
                                        plateNumber,
                                        distance,
                                        travelTime
                                );

                                arrLiveDrivers.add(liveDrivers);
                                arrLiveBus.add(liveBus);



                                // Add markers
                                runOnUiThread(() -> {
                                    removePassengerMarkers();
                                    busMarkers.add(MapsManager.addBusMarker(busLatLng, mMap));
                                });
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Collections.sort(arrLiveBus, new LiveBus.DistanceComparator());
                                    adapterBus.notifyDataSetChanged();

                                    if(arrLiveBus.isEmpty()){
                                        Log.d(TAG, "arrLiveBus is empty ");
                                        busListView.setVisibility(View.GONE);
                                        tvNoBus.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        Log.d(TAG, "arrLiveBus is not empty ");

                                        busListView.setVisibility(View.VISIBLE);
                                        tvNoBus.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                // Schedule the next update
                handler.postDelayed(this, UPDATE_INTERVAL);

        }
    };

    private void clickActions() {
        bottomNavigationView.setSelectedItemId(R.id.passenger_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.passenger_home) {
                return true;

            }
            else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;

            }
            else if (item.getItemId() == R.id.passenger_notification) {
                startActivity(new Intent(getApplicationContext(), PassengerNotification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;

            }

            return false;
        });

        tv_destination.setOnClickListener( v -> {

            startActivity(new Intent(this, BusSearchPage.class));
        });

        busListView.setOnItemClickListener( (adapterView, view, i, l) -> {

            if(TextUtils.isEmpty(tv_destination.getText())){
                Toast.makeText(this, "Please choose a destination. ", Toast.LENGTH_SHORT).show();
            }
            else {
                LiveBus liveBus = (LiveBus) adapterView.getItemAtPosition(i);
                showReserveSeatDialog(liveBus.getBusCode(), liveBus);
            }


        });

    }



    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
             ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

                if(mMap != null) mMap.setMyLocationEnabled(true);
                client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

                if(reservation.getDestination() != null){

                    if (busAsyncTask != null && busAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        busAsyncTask.cancel(true);
                    }

                    busAsyncTask = new BusAsyncTask(progressBar, busListView, tvNoBus);
                    busAsyncTask.execute();

                    Log.d(TAG, "Not empty Destination");


                    LatLng startLatLng = new LatLng(reservation.getStartLat(), reservation.getStartLong());
                    LatLng endLatLng = new LatLng(reservation.getDestinationLat(), reservation.getDestinationLong());
                    MapsManager.addStartAndEndMarker(startLatLng, endLatLng, mMap);
//                    MapsManager.drawPolyline(startLatLng, endLatLng, mMap);
                    tv_destination.setText(reservation.getDestination());

//                    LatLng destinationLatLng = new LatLng(reservation.getDestinationLat(), reservation.getDestinationLong());
//                    MapsManager.zoomCamera(destinationLatLng, mMap);

                }
                else {

                    Log.d(TAG, "Empty Destination");
                    zoomToUserLocation();
                    startBusUpdates();
                }


            }
            else{
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
        else {
            requestPermissions( new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    private void stopLocationUpdates() {
        client.removeLocationUpdates(locationCallback);
        deleteLocationInDb();
    }

    private void updateLocationInDb(){

        LivePassengers liveDrivers = new LivePassengers(My_USER_ID, lattitude, longitude, Helper.getCurrentDate());
        FirebaseManager.addData(livePassengerDb, liveDrivers, My_USER_ID);
    }

    private void deleteLocationInDb(){
        FirebaseManager.deleteData(livePassengerDb, My_USER_ID);
    }


    private void startBusUpdates() {
        isPassengerUpdatesRunning = true;
        handler.post(busUpdateRunnable);
    }

    private void stopBusDriverUpdates(){
        isPassengerUpdatesRunning = false;
        arrLiveBus.clear();
        arrLiveDrivers.clear();        
        handler.removeCallbacks(busUpdateRunnable);

        removePassengerMarkers();
    }

    private static void removePassengerMarkers() {

        if(!busMarkers.isEmpty() || busMarkers == null){
            for (Marker marker : busMarkers) {
                MapsManager.removeMarker(marker);
            }
            busMarkers.clear();
        }

    }

    private void showReserveSeatDialog(String busCode, LiveBus liveBus) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reserve Seat for " + busCode)
                .setMessage("Are you sure you want to reserve a bus seat?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String currentDate = Helper.getCurrentDate();
                        SeatReservation reservation = SeatReservation.getInstance();
                        reservation.setBusDriverId(liveBus.getBusDriverId());
                        reservation.setPassengerId(My_USER_ID);
                        reservation.setDateCreated(currentDate);
                        reservation.setBusCode(liveBus.getBusCode());
                        reservation.setRoute(liveBus.getRoute());
                        reservation.setPlateNumber(liveBus.getPlateNumber());
                        reservation.setStatus(ReservationStatus.PENDING);


                        if(reservation.getStartLocation().isEmpty() || reservation.getStartLocation() == ""){
                            Toast.makeText(PassengerBus.this, "Please choose starting location. ", Toast.LENGTH_LONG).show();
                        }
                        else if (reservation.getDestination().isEmpty() || reservation.getDestination() == ""){
                            Toast.makeText(PassengerBus.this, "Please choose destination. ", Toast.LENGTH_LONG).show();
                        }
                        else {

                            String relatedNodeId;

                            try {
                                relatedNodeId = String.valueOf(Helper.getDateInMillis(currentDate));
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            Notifications notifications = new Notifications(
                                    liveBus.getBusDriverId(),
                                    relatedNodeId,
                                    getResources().getString(R.string.seatReservationTitle),
                                    getResources().getString(R.string.seatReservationMessage),
                                    Helper.getCurrentDate(),
                                    NotificationTypes.RESERVATIONS
                            );


                            FirebaseManager.addData(seatReservationDb, reservation, relatedNodeId);
                            FirebaseManager.addData(notificationDb, notifications);
                            reservation.clear();
                            showConfirmationDialog();
                        }


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You reserved a bus seat. Wait for the confirmation by the driver if it is vacant or full.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tv_destination.setText(null);

                        stopBusDriverUpdates();
                        startBusUpdates();
                        zoomToUserLocation();
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


    

    @Override
    protected void onResume() {
        super.onResume();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");
        stopLocationUpdates();
        stopBusDriverUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop: ");
        stopLocationUpdates();
        stopBusDriverUpdates();
        reservation.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");
        stopLocationUpdates();
        stopBusDriverUpdates();
        reservation.clear();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        startLocationUpdates();

    }

    @SuppressLint("MissingPermission")
    private void zoomToUserLocation() {
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MapsManager.zoomCamera(myLatLng, mMap);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                address = place.getName();
                destinationLat = place.getLatLng().latitude;
                destinationLong = place.getLatLng().longitude;

//                MapsManager.addMarker(place.getLatLng(), address, mMap);

                tv_destination.setText(address);

            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            }
            else if (resultCode == RESULT_CANCELED) {
            }

            startBusUpdates();
        }

    }


    private void references() {
        progressBar = findViewById(R.id.progressBar);
        tv_destination = findViewById(R.id.tv_destination);
        tvNoBus = findViewById(R.id.tvNoBus);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        busListView = findViewById(R.id.busListView);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            Places.initialize(getApplicationContext(), apiKey);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class BusAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<ProgressBar> progressBarWeakReference;
        private WeakReference<ListView> listViewWeakReference;
        private WeakReference<TextView> textViewWeakReference;

        private ProgressBar progressBar;
        private ListView listView;
        private TextView noBusMessage;

        public BusAsyncTask(ProgressBar progressBar, ListView listView, TextView noBusMessage) {
            progressBarWeakReference = new WeakReference<>(progressBar);
            listViewWeakReference = new WeakReference<>(listView);
            textViewWeakReference = new WeakReference<>(noBusMessage);
            this.progressBar = progressBarWeakReference.get();
            this.listView = listViewWeakReference.get();
            this.noBusMessage = textViewWeakReference.get();

        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "BusAsyncTask started");

            try {
                Set<String> uniqueBusCodes = new HashSet<>();

                DatabaseReference busRoutesDb1 = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BUS_ROUTES.getValue());
                busRoutesDb1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        arrLiveDrivers.clear();
                        arrLiveBus.clear();

                        Log.d(TAG, "Snapshot exist: " + snapshot.exists());
                        if (snapshot.exists()) {

                            for (DataSnapshot stopSnapshot : snapshot.getChildren()) {

                                String stopName = stopSnapshot.child("StopName").getValue(String.class);
                                String busCode = stopSnapshot.child("BusCode").getValue(String.class);

                                String destination = reservation.getDestination();
                                String[] destinationSplit = destination.split(" ");
                                String keyWord = destinationSplit[0].toLowerCase().replace(",", "");

                                Log.d(TAG, "Stop name: " + stopName.toLowerCase());

                                if (stopName.toLowerCase().contains(keyWord)) {
                                    Log.d(TAG, "Keyword: " + keyWord);
                                    uniqueBusCodes.add(busCode);
                                }
                            }

                            arrBusCodes.addAll(uniqueBusCodes);
                            Log.d(TAG, "BusCode: " + uniqueBusCodes);

                            for (String busCode : uniqueBusCodes) {
                                retrieveLiveDrivers(busCode, listView, noBusMessage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error in doInBackground: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);

        }
    }

    private static void retrieveLiveDrivers(String busCode, ListView listView, TextView noBusMessage) {

        DatabaseReference liveDriversDb = FirebaseDatabase.getInstance().getReference().child(FirebaseReferences.LIVE_DRIVERS.getValue());

        liveDriversDb.orderByChild("busCode").equalTo(busCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for (DataSnapshot driverSnapshot : snapshot.getChildren()) {
                        LiveDrivers liveDrivers = driverSnapshot.getValue(LiveDrivers.class);

                        if (liveDrivers != null) {
                            LatLng busLatLng = new LatLng(liveDrivers.getLattitude(), liveDrivers.getLongitude());

                            String busDriverId = liveDrivers.getBusDriverId();
                            String route = liveDrivers.getRoute();
                            String busCode = liveDrivers.getBusCode();
                            String plateNumber = liveDrivers.getPlateNumber();
                            String distance = MapsManager.calculateDistance(myLatLng, busLatLng);
                            String travelTime = MapsManager.estimateTravelTime(myLatLng, busLatLng, apiKey);

                            LiveBus liveBus = new LiveBus(
                                    busDriverId,
                                    route,
                                    busCode,
                                    plateNumber,
                                    distance,
                                    travelTime
                            );

                            arrLiveDrivers.add(liveDrivers);
                            arrLiveBus.add(liveBus);

                            // Add markers
                            removePassengerMarkers();
                            busMarkers.add(MapsManager.addBusMarker(busLatLng, mMap));
                            MapsManager.zoomCamera(busLatLng, mMap);

                        }
                    }

                    Collections.sort(arrLiveBus, new LiveBus.DistanceComparator());
                    adapterBus.notifyDataSetChanged();

                    if(arrBusCodes.isEmpty()){
                        listView.setVisibility(View.GONE);
                        noBusMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        listView.setVisibility(View.VISIBLE);
                        noBusMessage.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }


}