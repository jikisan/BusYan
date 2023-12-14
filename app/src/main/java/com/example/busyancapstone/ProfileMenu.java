package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ProfileMenu extends AppCompatActivity {
    private MaterialButton editpro;
    private TextView logout, Help, aboutus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_menu);
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

        editpro = findViewById(R.id.editpro);
        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileMenu.this,Profile.class);
                startActivity(intent);

            }
        });

        //logout
        TextView logout  = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openLogoutActivity();

            }

            private void openLogoutActivity() {
                Intent intent = new Intent(ProfileMenu.this, LogoutActivity.class);
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
                Intent intent = new Intent(ProfileMenu.this, AboutUs.class);
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
                Intent intent = new Intent(ProfileMenu.this, HelpCenter.class);
                startActivity(intent);
            }
        });


    }
}