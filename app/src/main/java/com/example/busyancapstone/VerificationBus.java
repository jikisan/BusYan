package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class VerificationBus extends AppCompatActivity {

    private MaterialButton verifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_bus);

        verifybtn = (MaterialButton) findViewById(R.id.verifybtn);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLoginPage();

            }

            private void openLoginPage() {
                Intent intent = new Intent(VerificationBus.this, LoginPageBus.class);
                startActivity(intent);
            }
        });


    }
}