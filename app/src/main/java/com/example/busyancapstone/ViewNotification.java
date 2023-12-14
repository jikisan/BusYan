package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Enum.NotificationTypes;
import com.example.busyancapstone.Enum.ReservationStatus;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Notifications;
import com.example.busyancapstone.Model.Passenger;
import com.example.busyancapstone.Model.SeatReservation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewNotification extends AppCompatActivity {

    private  BottomNavigationView bottomNavigationView;
    private ImageView iv_image;
    private Button btnAccept, btnFull, btnCancel;
    private TextView tvStart, tvDestination, name;

    private final String My_USER_ID = FirebaseManager.getMyUserId();
    private String reservationId, passengerId, busCode;
    private SeatReservation seatReservation;

    private DatabaseReference seatReserveDb, notificationDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);

        reservationId = getIntent().getExtras().getString("reservationId");

        seatReserveDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESERVATIONS.getValue());
        notificationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.NOTIFICATIONS.getValue());


        references();
        clickActions();
        generateDetails();

    }

    private void generateDetails() {

        if(reservationId != null) {
            seatReserveDb.child(reservationId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        seatReservation = snapshot.getValue(SeatReservation.class);
                        passengerId = seatReservation.getPassengerId();

                        generatePassengerName(name, passengerId);
                        busCode = seatReservation.getBusCode();

                        tvStart.setText(seatReservation.getStartLocation());
                        tvDestination.setText(seatReservation.getDestination());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void generatePassengerName(TextView name, String passengerId) {

        DatabaseReference passengerDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.PASSENGER.getValue());

        passengerDb.child(passengerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Passenger passenger = snapshot.getValue(Passenger.class);
                    name.setText(Helper.capitalizeFirstLetter(passenger.getFullName()));

                    String imageUrl = passenger.getImageUrl();
                    if(imageUrl != null) {
                        Picasso.get()
                                .load(imageUrl)
                                .fit()
                                .placeholder(R.drawable.passengerpic)
                                .into(iv_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void clickActions() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_notification);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_notification) {

                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileMenu.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), BusActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        btnAccept.setOnClickListener( view -> {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", ReservationStatus.ACCEPT);

            FirebaseManager.updateData(seatReserveDb, reservationId, hashMap);
            Toast.makeText(this, "Reservation is accepted!", Toast.LENGTH_SHORT).show();

            String title = getResources().getString(R.string.seatReservationAcceptTitle);
            String message = getResources().getString(R.string.seatReservationAcceptMessage);
            createNotif(title, message, NotificationTypes.RESERVATIONS_ACCEPTED);
        });

        btnFull.setOnClickListener( view -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", ReservationStatus.FULL);

            FirebaseManager.updateData(seatReserveDb, reservationId, hashMap);

            Toast.makeText(this, "Passenger is notified that the bus is already full. ", Toast.LENGTH_SHORT).show();

            String title = getResources().getString(R.string.seatReservationFullTitle);
            String message = getResources().getString(R.string.seatReservationFullMessage);
            createNotif(title, message, NotificationTypes.RESERVATIONS_FULL);
        });

        btnCancel.setOnClickListener( view -> {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", ReservationStatus.CANCEL);

            FirebaseManager.updateData(seatReserveDb, reservationId, hashMap);

            Toast.makeText(this, "Reservation is cancelled", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, BusStartingPage.class));

            String title = getResources().getString(R.string.seatReservationCancelTitle);
            String message = getResources().getString(R.string.seatReservationCancelMessage);
            createNotif(title, message, NotificationTypes.RESERVATIONS_CANCEL);
        });

    }

    private void references() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        iv_image = findViewById(R.id.iv_image);

        tvStart = findViewById(R.id.tv_start);
        tvDestination = findViewById(R.id.tv_destination);
        name = findViewById(R.id.name);

        btnAccept = findViewById(R.id.btnAccept);
        btnFull = findViewById(R.id.btnFull);
        btnCancel = findViewById(R.id.btnCancel);

    }

    private void createNotif(String title, String notifMessage, NotificationTypes notificationTypes){

        Notifications notifications = new Notifications(
                passengerId,
                reservationId,
                title,
                notifMessage,
                Helper.getCurrentDate(),
                notificationTypes
        );


        FirebaseManager.addData(notificationDb, notifications);
    }
}