package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutTheCompany extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_company);

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

        //job description
        TextView JobDes  = findViewById(R.id.JobDes);
        JobDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBusDescription();

            }

            private void openBusDescription() {
                Intent intent = new Intent(AboutTheCompany.this, BusDescription.class);
                startActivity(intent);
            }
        });

        // about the company

        TextView AboutCom = findViewById(R.id.AboutCom);
        AboutCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAboutTheCompany();

            }

            private void openAboutTheCompany() {
                Intent intent = new Intent(AboutTheCompany.this, AboutTheCompany.class);
                startActivity(intent);
            }
        });

    }
}