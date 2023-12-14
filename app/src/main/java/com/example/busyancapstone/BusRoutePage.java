package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BusRoutePage extends AppCompatActivity implements View.OnClickListener {

    private ImageView busBtn, locationBtn;
    private CardView thirteenc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_page);

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


        // busbtn
        ImageView busBtn  = findViewById(R.id.busBtn);
        busBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBusRoutePage();

            }

            private void openBusRoutePage() {
                Intent intent = new Intent(BusRoutePage.this, BusRoutePage.class);
                startActivity(intent);
            }
        });

        ImageView locationBtn  = findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBusSearchPage();

            }

            private void openBusSearchPage() {
                Intent intent = new Intent(BusRoutePage.this, BusSearchPage.class);
                startActivity(intent);
            }
        });

        thirteenc = findViewById(R.id.thirteenc_card);

        thirteenc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        
        openBusRouteDescription();
        
    }

    private void openBusRouteDescription() {
        Intent intent = new Intent(BusRoutePage.this, BusRouteDescription.class);
        startActivity(intent);
    }
}