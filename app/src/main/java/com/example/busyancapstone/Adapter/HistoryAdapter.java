package com.example.busyancapstone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.busyancapstone.Model.SeatReservation;
import com.example.busyancapstone.R;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private List<SeatReservation> reservations;
    private Context context;

    public HistoryAdapter(List<SeatReservation> reservations, Context context) {
        this.reservations = reservations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        }

        // Get the item for the current position
        SeatReservation item = reservations.get(i);

        // Set the item's text
        TextView tv_busCode = view.findViewById(R.id.tv_busCode);
        TextView tv_location = view.findViewById(R.id.tv_location);
        TextView tv_destination = view.findViewById(R.id.tv_destination);
        TextView tv_date = view.findViewById(R.id.tv_date);


        tv_busCode.setText(item.getBusCode());
        tv_location.setText("Location: " + item.getStartLocation());
        tv_destination.setText("Destination: " + item.getDestination());
        tv_date.setText(item.getDateCreated());

        // Return the view
        return view;
    }
}
