package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Passenger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PassengerProfile extends AppCompatActivity {

    private final String MY_USER_ID = FirebaseManager.getMyUserId();
    private MaterialButton editpro;
    private TextView logout, Help, aboutus, tv_email, tv_name;
    private ImageView iv_profilePic;
    private DatabaseReference passengerDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        String dbName = FirebaseReferences.PASSENGER.getValue();
        passengerDb = FirebaseDatabase.getInstance().getReference(dbName);

        tv_email = findViewById(R.id.tv_email);
        tv_name = findViewById(R.id.tv_name);
        iv_profilePic = findViewById(R.id.iv_profilePic);
        
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.passenger_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.passenger_profile) {
                return true;
            } else if (item.getItemId() == R.id.passenger_home) {
                startActivity(new Intent(getApplicationContext(), PassengerActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.passenger_notification) {
                startActivity(new Intent(getApplicationContext(), PassengerNotification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        editpro = findViewById(R.id.editpro);
        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PassengerProfile.this, PassengerEditProfile.class);
                startActivity(intent);

            }
        });


        //saved
        TextView saved = findViewById(R.id.saved);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSavedPage();

            }

            private void openSavedPage() {
                Intent intent = new Intent(PassengerProfile.this, SavedPage.class);
                startActivity(intent);
            }
        });


        //logout
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openLoginPageBus();

            }

            private void openLoginPageBus() {
                Intent intent = new Intent(PassengerProfile.this, LogoutBus.class);
                startActivity(intent);
            }
        });

        //about us

        TextView aboutus = findViewById(R.id.aboutus);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAboutUs();

            }

            private void openAboutUs() {
                Intent intent = new Intent(PassengerProfile.this, AboutUs.class);
                startActivity(intent);
            }
        });


        // help center
        TextView Help = findViewById(R.id.Help);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHelpCenter();

            }

            private void openHelpCenter() {
                Intent intent = new Intent(PassengerProfile.this, HelpCenter.class);
                startActivity(intent);
            }
        });

        getData();
    }

    private void getData() {

        passengerDb.child(MY_USER_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Passenger passenger = snapshot.getValue(Passenger.class);
                    Picasso.get()
                            .load(passenger.getImageUrl())
                            .placeholder(R.drawable.passengerpic)
                            .into(iv_profilePic);

                    tv_name.setText(passenger.getFullName());
                    tv_email.setText(passenger.getEmail());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}