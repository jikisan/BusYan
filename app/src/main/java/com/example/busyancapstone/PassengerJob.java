package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class PassengerJob extends AppCompatActivity {


    private TextView tv_search;
    private BottomNavigationView bottomNavigationView;
    private ImageView iv_tempAddJobBtn;
    private MaterialButton btn_busConductor, btn_busDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_job);

        references();
        clickActions();

    }

    private void clickActions() {

        bottomNavigationView.setSelectedItemId(R.id.passenger_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.passenger_home) {
                return true;
            }
            else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.passenger_notification) {
                startActivity(new Intent(getApplicationContext(), PassengerNotification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PassengerJob.this, PassengerSearchActivity.class);
                startActivity(intent);
            }
        });

        btn_busDriver.setOnClickListener(v -> {
            Intent intent = new Intent(PassengerJob.this, PassengerSearchActivity.class);
            intent.putExtra("category", "Driver");
            startActivity(intent);
        });

        btn_busConductor.setOnClickListener(v -> {
            Intent intent = new Intent(PassengerJob.this, PassengerSearchActivity.class);
            intent.putExtra("category", "Conductor");
            startActivity(intent);
        });

//        iv_tempAddJobBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(PassengerJob.this, AddJob.class));
//            }
//        });
    }

    private void references() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tv_search  = findViewById(R.id.tv_search);
        btn_busDriver  = findViewById(R.id.btn_busDriver);
        btn_busConductor  = findViewById(R.id.btn_busConductor);
        iv_tempAddJobBtn  = findViewById(R.id.iv_tempAddJobBtn);
    }
}