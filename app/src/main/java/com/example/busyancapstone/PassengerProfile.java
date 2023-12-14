package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class PassengerProfile extends AppCompatActivity {

    private MaterialButton editpro;
    private TextView logout, aboutus, Help, saved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);


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
        TextView saved  = findViewById(R.id.saved);
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
        TextView logout  = findViewById(R.id.logout);
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

        TextView aboutus  = findViewById(R.id.aboutus);
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
        TextView Help  = findViewById(R.id.Help);
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



    }
}