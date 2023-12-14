package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.busyancapstone.Adapter.CustomAdapterNotifBusDriver;
import com.example.busyancapstone.Adapter.CustomAdapterNotifPassenger;
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
import java.util.Collection;
import java.util.Collections;

public class PassengerNotification extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ListView notifListView;

    private String passengerId;
    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    private CustomAdapterNotifPassenger customAdapterNotif;
    private final ArrayList<Notifications> arrNotif = new ArrayList<>();

    private DatabaseReference notifDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_notification);

        notifDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.NOTIFICATIONS.getValue());

        references();
        clickActions();

        customAdapterNotif = new CustomAdapterNotifPassenger(PassengerNotification.this, arrNotif);
        notifListView.setAdapter(customAdapterNotif);

        generateNotif();

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
                startActivity(new Intent(getApplicationContext(), BusActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        notifListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

    }

    private void generateNotif() {

        Query query = notifDb.orderByChild("targetUserId").equalTo(MY_USER_ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrNotif.clear(); // Clear existing data

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Notifications notifications = dataSnapshot.getValue(Notifications.class);
                        arrNotif.add(notifications);

                        Log.d("NotificationActivity", "Notification ID: " + notifications.getRelatedNodeId());

                    }

                    Collections.reverse(arrNotif);
                    customAdapterNotif.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

    private void references() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        notifListView = findViewById(R.id.notifListView);


    }

}