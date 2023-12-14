package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReserveActivity extends AppCompatActivity {

    private TextView No, Yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

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

        TextView Yes  = findViewById(R.id.Yes);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openReserveConfirmed();

            }

            private void openReserveConfirmed() {
                Intent intent = new Intent(ReserveActivity.this, ReserveConfirmed.class);
                startActivity(intent);
            }
        });

        TextView No  = findViewById(R.id.No);
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPassengerBus();

            }

            private void openPassengerBus() {
                Intent intent = new Intent(ReserveActivity.this, PassengerBus.class);
                startActivity(intent);
            }
        });


    }
}