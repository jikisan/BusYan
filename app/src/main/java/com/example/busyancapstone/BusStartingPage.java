package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Manager.MapsManager;
import com.example.busyancapstone.Model.BusDriver;
import com.example.busyancapstone.Model.LiveDrivers;
import com.example.busyancapstone.Model.LivePassengers;
import com.example.busyancapstone.Model.RevisionRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusStartingPage extends AppCompatActivity implements OnMapReadyCallback {

    private TextView plateTextView, plateEditText;
    private LinearLayout layout;
    private MaterialButton locationOff;
    private MaterialButton locationOn;
    private MaterialButton busCodeButton;
    private MaterialButton setRouteButton;
    private BottomNavigationView bottomNavigationView;
    private SupportMapFragment mapFragment;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private double lattitude = 0.0, longitude = 0.0;
    private boolean isPassengerUpdatesRunning = false;
    private String apiKey, busCode, route, plateNum;

    private final String My_USER_ID = FirebaseManager.getMyUserId();
    private ArrayList<LivePassengers> arrLivePassengers = new ArrayList<>();
    private ArrayList<Marker> passengerMarkers = new ArrayList<>();

    private DatabaseReference liveDriversDb, busDriverDb, livePassengerDb, revisionRequestDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_starting_page);

        busDriverDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BUS_DRIVER.getValue());
        liveDriversDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.LIVE_DRIVERS.getValue());
        livePassengerDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.LIVE_PASSENGERS.getValue());
        revisionRequestDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.REVISION_REQUEST.getValue());


        references();
        showConfirmationDialog();
        getDriverData();

    }

    private void setLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        locationRequest.setFastestInterval(15000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

//    LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            if (mMap != null) {
//                Location location = locationResult.getLastLocation();
//                lattitude = location.getLatitude();
//                longitude = location.getLongitude();
//                LatLng latLng = new LatLng(lattitude, longitude);
////                MapsManager.zoomCamera(latLng, mMap);
//                updateLocationInDb();
//            }
//        }
//    };

    private void clickActions() {

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {

                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileMenu.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_notification) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        locationOff.setOnClickListener(v -> {
            locationOn.setVisibility(View.VISIBLE);
            locationOff.setVisibility(View.GONE);

            startLocationUpdates();
            isPassengerUpdatesRunning = true;
            startPassengerUpdates();
        });

        locationOn.setOnClickListener(v -> {
            locationOn.setVisibility(View.GONE);
            locationOff.setVisibility(View.VISIBLE);

            stopLocationUpdates();
            stopPassengerUpdates();

        });
    }

    private void getDriverData() {

        busDriverDb.child(My_USER_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    BusDriver busDriver = snapshot.getValue(BusDriver.class);

                    busCode = busDriver.getBusCode();
                    route = busDriver.getRoute();
                    plateNum = busDriver.getPlateNumber();

                    busCodeButton.setText(busCode);
                    setRouteButton.setText(route);
                    plateEditText.setText(plateNum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Yes if your Bus details are accurate. If No, ask your bus operator to make revision and edits")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(BusStartingPage.this, "Revision request has been sent to the operator", Toast.LENGTH_LONG).show();
                        createRevisionRequest();
                        startActivity(new Intent(BusStartingPage.this, MainActivity.class));
                        dialog.dismiss();
                    }


                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createRevisionRequest() {

        RevisionRequest request = new RevisionRequest(
                My_USER_ID,
                Helper.getCurrentDate(),
                ""
        );

        FirebaseManager.addData(revisionRequestDb, request);
    }


    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

                setLocationRequest();

                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (mMap != null) {
                            Location location = locationResult.getLastLocation();
                            lattitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng latLng = new LatLng(lattitude, longitude);
//                MapsManager.zoomCamera(latLng, mMap);
                            updateLocationInDb();
                        }
                    }
                };

                if(mMap != null) mMap.setMyLocationEnabled(true);
                zoomToUserLocation();
                client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
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

        LiveDrivers liveDrivers = new LiveDrivers(
                My_USER_ID,
                route,
                busCode,
                plateNum,
                lattitude,
                longitude,
                Helper.getCurrentDate());
        FirebaseManager.addData(liveDriversDb, liveDrivers, My_USER_ID);
    }

    private void deleteLocationInDb(){
        FirebaseManager.deleteData(liveDriversDb, My_USER_ID);
    }



    private void startPassengerUpdates(){

        livePassengerDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!isPassengerUpdatesRunning) {
                    return;
                }

                removePassengerMarkers();


                if(snapshot.hasChildren()){

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                        LivePassengers livePassengers = dataSnapshot.getValue(LivePassengers.class);
                        arrLivePassengers.add(livePassengers);

                        LatLng latLng = new LatLng(livePassengers.getLattitude(), livePassengers.getLongitude());
                        passengerMarkers.add(MapsManager.addPassengerMarker(latLng, mMap));

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void stopPassengerUpdates(){
        isPassengerUpdatesRunning = false;
        removePassengerMarkers();
    }

    private void removePassengerMarkers() {
        for (Marker marker : passengerMarkers) {
            MapsManager.removeMarker(marker);
        }
        passengerMarkers.clear();
    }


    @Override
    protected void onResume() {
        super.onResume();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        setLocationRequest();
//        startLocationUpdates();
//        startPassengerUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        stopLocationUpdates();
//        stopPassengerUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopLocationUpdates();
//        stopPassengerUpdates();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        clickActions();

    }

    @SuppressLint("MissingPermission")
    private void zoomToUserLocation() {
        Task<Location> locationTask = client.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapsManager.zoomCamera(latLng, mMap);
            }
        });
    }

    private void references() {

        plateEditText = findViewById(R.id.plate);
        layout = findViewById(R.id.lyout);
        locationOff = findViewById(R.id.locationOff);
        locationOn = findViewById(R.id.locationOn);
        busCodeButton = findViewById(R.id.buscode);
        setRouteButton = findViewById(R.id.setroute);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
}