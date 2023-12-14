package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class Startingpage1 extends AppCompatActivity {
    private MaterialButton nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startingpage1);

      nextbtn = findViewById(R.id.nextbtn);
      nextbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              Intent intent = new Intent(Startingpage1.this, MainActivity.class);
              startActivity(intent);

          }
      });



    }
}