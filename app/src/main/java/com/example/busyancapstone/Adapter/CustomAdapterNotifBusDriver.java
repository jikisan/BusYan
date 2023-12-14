package com.example.busyancapstone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Model.Notifications;
import com.example.busyancapstone.Model.TempNotifBusDriver;
import com.example.busyancapstone.R;
import com.example.busyancapstone.ViewNotification;

import java.util.List;

public class CustomAdapterNotifBusDriver extends BaseAdapter {

    private Context context;
    private List<TempNotifBusDriver> arrTempBus;

    public CustomAdapterNotifBusDriver() {
    }

    public CustomAdapterNotifBusDriver(Context context, List<TempNotifBusDriver> arrTempBus) {
        this.context = context;
        this.arrTempBus = arrTempBus;
    }

    @Override
    public int getCount() {
        return arrTempBus.size();
    }

    @Override
    public Object getItem(int i) {
        return arrTempBus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_notifications, null);
        }

        TextView tv_passengerName = view.findViewById(R.id.tv_passengerName);
        TextView tv_timeDiff = view.findViewById(R.id.tv_timeDiff);
        TextView tv_viewButton = view.findViewById(R.id.tv_viewBtn);

        String timeDiff = Helper.getTimeDiff(Helper.getCurrentDate(), arrTempBus.get(i).getDateCreated());
        String passengerName = Helper.capitalizeFirstLetter(arrTempBus.get(i).getPassengerName());
        String message = context.getResources().getString(R.string.seatReservationMessage);

        tv_passengerName.setText(passengerName + " " + message);
        tv_timeDiff.setText(timeDiff);

        tv_viewButton.setOnClickListener( v -> {

            Intent intent = new Intent(context, ViewNotification.class);
            intent.putExtra("reservationId", arrTempBus.get(i).getSeatReservationId());
            context.startActivity(intent);
        });

        return view;

    }

//    private void getPassengerName(Notifications notifications, TextView tvPassengerName, TextView tvTimeDiff, int i) {
//
//        String id = notifications.getId();
//
//    }
}
