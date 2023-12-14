package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView LoginPage, LoginPageBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginPageBus= findViewById(R.id.BusDriver_card);
        LoginPage= findViewById(R.id.Passenger_card);

        LoginPageBus.setOnClickListener(this);
        LoginPage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent i;

        if (view.getId() == R.id.BusDriver_card) {
            i = new Intent(this, com.example.busyancapstone.LoginPageBus.class);
            startActivity(i);
        } else if (view.getId() == R.id.Passenger_card) {
            i = new Intent(this, com.example.busyancapstone.LoginPage.class);
            startActivity(i);
        }

    }
}