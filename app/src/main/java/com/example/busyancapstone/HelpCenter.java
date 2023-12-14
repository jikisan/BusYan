package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelpCenter extends AppCompatActivity {


    private TextView trackbus, searchjob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile) {

                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), BusActivity.class));
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

        TextView trackbus = findViewById(R.id.trackbus);
        trackbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHowActivity();

            }

            private void openHowActivity() {
                Intent intent = new Intent(HelpCenter.this, HowActivity.class);
                startActivity(intent);
            }
        });

        TextView searchjob = findViewById(R.id.searchjob);
        searchjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHowActivity2();

            }

            private void openHowActivity2() {
                Intent intent = new Intent(HelpCenter.this, HowActivity2.class);
                startActivity(intent);
            }
        });


    }
}