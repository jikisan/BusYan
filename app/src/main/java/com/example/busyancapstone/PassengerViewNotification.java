package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Enum.ReservationStatus;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Manager.MapsManager;
import com.example.busyancapstone.Model.BusDriver;
import com.example.busyancapstone.Model.LiveDrivers;
import com.example.busyancapstone.Model.SeatReservation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class PassengerViewNotification extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView iv_image;
    private Button btnCancel;
    private ProgressBar progress_circular;
    private TextView tvBusCode, tv_distance, tvArrivalTime, tvPlateNum, tvName;

    private DatabaseReference seatReservationDb, liveDriversDb, busDriverDb;
    private String reservationId, apiKey;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_view_notification);

        reservationId = getIntent().getExtras().getString("reservationId");

        seatReservationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESERVATIONS.getValue());
        liveDriversDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.LIVE_DRIVERS.getValue());
        busDriverDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BUS_DRIVER.getValue());

        references();
        clickActions();
        generateBusData();


    }

    private void clickActions() {

        bottomNavigationView.setSelectedItemId(R.id.passenger_notification);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.passenger_notification) {

                return true;
            }
            else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            else if (item.getItemId() == R.id.passenger_home) {
                startActivity(new Intent(getApplicationContext(), PassengerActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        btnCancel.setOnClickListener( view -> {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", ReservationStatus.CANCEL);

            FirebaseManager.updateData(seatReservationDb, reservationId, hashMap);

            Toast.makeText(this, "Reservation is cancelled", Toast.LENGTH_SHORT).show();

        });
    }

    private void generateBusData() {

        progress_circular.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if(reservationId != null) {
                    seatReservationDb.child(reservationId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists()){

                                SeatReservation seatReservation = snapshot.getValue(SeatReservation.class);

                                LatLng myLatLng = new LatLng(seatReservation.getStartLat(), seatReservation.getStartLong());

                                String busCode = seatReservation.getBusCode();
                                String plateNumber = seatReservation.getPlateNumber();
                                String busDriverId = seatReservation.getBusDriverId();
                                ReservationStatus status = seatReservation.getStatus();

                                if(status == ReservationStatus.PENDING){
                                    btnCancel.setVisibility(View.VISIBLE);
                                }

                                tvBusCode.setText(busCode);
                                tvPlateNum.setText(plateNumber);

                                generateBusDriverName(busDriverId, myLatLng);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                handler.postDelayed(this, 10000);
            }
        };

        handler.postDelayed(runnable, 10000);


    }

    private void generateBusDriverName(String busDriverId, LatLng myLatLng) {

        busDriverDb.child(busDriverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    BusDriver busDriver = snapshot.getValue(BusDriver.class);

                    tvName.setText(Helper.capitalizeFirstLetter(busDriver.getFullName()));

                    String imageUrl = busDriver.getImageUrl();
                    if(imageUrl != null) {
                        Picasso.get()
                                .load(imageUrl)
                                .fit()
                                .placeholder(R.drawable.iconprofile)
                                .into(iv_image);
                    }

                    generateTravelData(busDriverId, myLatLng);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateTravelData(String busDriverId, LatLng myLatLng) {

        if (apiKey == null) {
            return;
        }

        liveDriversDb.child(busDriverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    LiveDrivers liveDrivers = snapshot.getValue(LiveDrivers.class);

                    LatLng busLatLng = new LatLng(liveDrivers.getLattitude(), liveDrivers.getLongitude());
                    String busDriverId = liveDrivers.getBusDriverId();
                    String distance = MapsManager.calculateDistance(myLatLng, busLatLng);
                    String travelTime = MapsManager.estimateTravelTime(myLatLng, busLatLng, apiKey);


                    tv_distance.setText(distance);
                    tvArrivalTime.setText(travelTime);
                    progress_circular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void references() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        iv_image = findViewById(R.id.iv_image);

        tvBusCode = findViewById(R.id.tvBusCode);
        tvName = findViewById(R.id.tvName);
        tv_distance = findViewById(R.id.tv_distance);
        tvArrivalTime = findViewById(R.id.tvArrivalTime);
        tvPlateNum = findViewById(R.id.tvPlateNum);

        btnCancel = findViewById(R.id.btnCancel);
        progress_circular = findViewById(R.id.progress_circular);

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            Places.initialize(getApplicationContext(), apiKey);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start the initial execution of the code
        handler.postDelayed(runnable, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the execution when the activity is paused to avoid memory leaks
        handler.removeCallbacks(runnable);
    }
}