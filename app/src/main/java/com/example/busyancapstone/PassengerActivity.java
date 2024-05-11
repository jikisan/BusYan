package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView PassengerBus, PassengerJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        PassengerBus= findViewById(R.id.buspass_card);
        PassengerJob= findViewById(R.id.job_card);

        PassengerBus.setOnClickListener(this);
        PassengerJob.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.passenger_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.passenger_home) {
                return true;

            } else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
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
    }

    @Override
    public void onClick(View view) {

        Intent i;

        if (view.getId() == R.id.buspass_card) {
            i = new Intent(this, com.example.busyancapstone.PassengerBus.class);
            startActivity(i);
        } else if (view.getId() == R.id.job_card) {
            i = new Intent(this, com.example.busyancapstone.PassengerJob.class);
            startActivity(i);
        }

    }
}