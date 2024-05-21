package com.example.busyancapstone;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busyancapstone.Adapter.HistoryAdapter;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Model.SeatReservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryPage extends AppCompatActivity {

    private List<SeatReservation> seatReservationList = new ArrayList<>();
    private DatabaseReference reservationDb;
    private ListView historyListview;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        reservationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESERVATIONS.getValue());

        historyListview = findViewById(R.id.historyListview);
        backBtn = findViewById(R.id.backBtn);

        HistoryAdapter adapter = new HistoryAdapter(seatReservationList, this);
        historyListview.setAdapter(adapter);

        reservationDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                seatReservationList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    SeatReservation seatReservation = snapshot1.getValue(SeatReservation.class);
                    seatReservationList.add(seatReservation);
                }

                Collections.reverse(seatReservationList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backBtn.setOnClickListener(v -> {

            Intent intent = new Intent(this, PassengerProfile.class);
            startActivity(intent);
        });


    }
}