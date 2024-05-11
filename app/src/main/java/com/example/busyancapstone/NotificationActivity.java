package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.busyancapstone.Adapter.CustomAdapterNotifBusDriver;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Notifications;
import com.example.busyancapstone.Model.Passenger;
import com.example.busyancapstone.Model.SeatReservation;
import com.example.busyancapstone.Model.TempNotifBusDriver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView notifListView;

    private String passengerId;
    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    private CustomAdapterNotifBusDriver customAdapterNotif;
    private final ArrayList<TempNotifBusDriver> arrTempBus = new ArrayList<>();

    private DatabaseReference notifDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notifDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.NOTIFICATIONS.getValue());

        references();
        clickActions();

        customAdapterNotif = new CustomAdapterNotifBusDriver(NotificationActivity.this, arrTempBus);
        notifListView.setAdapter(customAdapterNotif);

        generateNotif();

    }

    private void generateNotif() {

        Query query = notifDb.orderByChild("targetUserId").equalTo(MY_USER_ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrTempBus.clear(); // Clear existing data

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Notifications notifications = dataSnapshot.getValue(Notifications.class);

                        Log.d("NotificationActivity", "Notification ID: " + notifications.getRelatedNodeId());
                        generateReservationData(notifications.getRelatedNodeId());
                    }
                    // Notify the adapter that the data has changed
                    customAdapterNotif.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

    private void generateReservationData(String id) {
        DatabaseReference seatReservationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESERVATIONS.getValue());
        DatabaseReference passengerDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.PASSENGER.getValue());

        seatReservationDb.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SeatReservation seatReservation = snapshot.getValue(SeatReservation.class);
                    String busDriverId = seatReservation.getBusDriverId();
                    String dateCreated = seatReservation.getDateCreated();
                    String seatReservationId = snapshot.getKey();

                    Log.d("NotificationActivity", "Seat Reservation ID: " + seatReservationId);

                    passengerId = seatReservation.getPassengerId();

                    if (busDriverId.equalsIgnoreCase(MY_USER_ID)) {
                        passengerDb.child(passengerId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Passenger passenger = snapshot.getValue(Passenger.class);
                                    Log.d("NotificationActivity", "Passenger Name: " + passenger.getFullName());

                                    TempNotifBusDriver tempNotifBusDriver = new TempNotifBusDriver(
                                            passenger.getFullName(),
                                            dateCreated,
                                            seatReservationId
                                    );

                                    arrTempBus.add(tempNotifBusDriver);
                                }

                                customAdapterNotif.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("NotificationActivity", "Error fetching passenger data", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotificationActivity", "Error fetching seat reservation data", error.toException());
            }
        });
    }

    private void clickActions() {

        bottomNavigationView.setSelectedItemId(R.id.bottom_notification);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_notification) {

                return true;
            }
            else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileMenu.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), BusStartingPage.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

    }

    private void references() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        notifListView = findViewById(R.id.notifListView);


    }

}