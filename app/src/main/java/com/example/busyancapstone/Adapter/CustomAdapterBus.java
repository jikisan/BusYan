package com.example.busyancapstone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.busyancapstone.Manager.MapsManager;
import com.example.busyancapstone.Model.LiveBus;
import com.example.busyancapstone.Model.LiveDrivers;
import com.example.busyancapstone.Model.TempJob;
import com.example.busyancapstone.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class CustomAdapterBus extends BaseAdapter {

    private Context context;
    private List<LiveBus> arrLiveBus;
    private LatLng myLatlng;
    private String apiKey;

    public CustomAdapterBus(Context context, List<LiveBus> arrLiveBus, LatLng myLatlng, String apiKey) {
        this.context = context;
        this.arrLiveBus = arrLiveBus;
        this.myLatlng = myLatlng;
        this.apiKey = apiKey;
    }

    @Override
    public int getCount() {
        return Math.min(arrLiveBus.size(), 2);
    }

    @Override
    public Object getItem(int i) {
        return arrLiveBus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_buses, null);
        }

            TextView tv_busCode = convertView.findViewById(R.id.tv_busCode);
            TextView tv_distance = convertView.findViewById(R.id.tv_distance);
            TextView tv_travelTime = convertView.findViewById(R.id.tv_travelTime);

            tv_busCode.setText(arrLiveBus.get(i).getBusCode());
            tv_distance.setText(arrLiveBus.get(i).getDistance());
            tv_travelTime.setText(arrLiveBus.get(i).getTravelTime());


        return convertView;
    }

    // Custom method to clear the adapter's data
    public void clearData() {
        arrLiveBus.clear();
        notifyDataSetChanged();
    }
}
