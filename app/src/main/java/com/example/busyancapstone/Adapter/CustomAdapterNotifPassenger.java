package com.example.busyancapstone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Enum.NotificationTypes;
import com.example.busyancapstone.Enum.ReservationStatus;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Model.Notifications;
import com.example.busyancapstone.Model.Passenger;
import com.example.busyancapstone.Model.SeatReservation;
import com.example.busyancapstone.Model.TempNotifBusDriver;
import com.example.busyancapstone.PassengerViewNotification;
import com.example.busyancapstone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomAdapterNotifPassenger extends BaseAdapter {

    private Context context;
    private List<Notifications> arrNotif;
    private String seatReservationId;

    public CustomAdapterNotifPassenger() {
    }

    public CustomAdapterNotifPassenger(Context context, List<Notifications> arrNotif) {
        this.context = context;
        this.arrNotif = arrNotif;
    }

    @Override
    public int getCount() {
        return arrNotif.size();
    }

    @Override
    public Object getItem(int i) {
        return arrNotif.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_notif_passenger_reservation, null);
        }

        TextView tv_notifMessage = view.findViewById(R.id.tv_notifMessage);
        TextView tv_timeDiff = view.findViewById(R.id.tv_timeDiff);

        String timeDiff = Helper.getTimeDiff(Helper.getCurrentDate(), arrNotif.get(i).getDateCreated());
        tv_timeDiff.setText(timeDiff);

        switch(arrNotif.get(i).getNotifType()){
            case RESERVATIONS_ACCEPTED:
            case RESERVATIONS_CANCEL:
            case RESERVATIONS_FULL:
                getSeatReservation(arrNotif.get(i), tv_notifMessage, tv_timeDiff, i);
                break;
            case JOBS:
                break;
        }

        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, PassengerViewNotification.class);
            intent.putExtra("reservationId", seatReservationId);
            context.startActivity(intent);
        });

        return view;
    }

    private void getSeatReservation(Notifications notifications, TextView tvNotifMessage, TextView tvTimeDiff, int i) {

        DatabaseReference seatReservationDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.RESERVATIONS.getValue());

        seatReservationDb.child(notifications.getRelatedNodeId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    SeatReservation seatReservation = snapshot.getValue(SeatReservation.class);

                    seatReservationId = snapshot.getKey();
                    String busCode = seatReservation.getBusCode();
                    String message = busCode + " " + notifications.getMessage();

                    String dateCreated = seatReservation.getDateCreated();

                    tvNotifMessage.setText(message);

                    Log.d("NotificationActivity", "Seat Reservation ID: " + seatReservationId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotificationActivity", "Error fetching seat reservation data", error.toException());
            }
        });
    }

}

