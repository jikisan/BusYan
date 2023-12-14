package com.example.busyancapstone.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Bookmark;
import com.example.busyancapstone.Model.LiveBus;
import com.example.busyancapstone.Model.PlacesInfo;
import com.example.busyancapstone.Model.SavedAddress;
import com.example.busyancapstone.Model.TempJob;
import com.example.busyancapstone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterSearchAddress  extends BaseAdapter {

    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    private Context context;
    private List<PlacesInfo> arrAddress;

    public CustomAdapterSearchAddress() {
    }

    public CustomAdapterSearchAddress(Context context, List<PlacesInfo> arrAddress) {
        this.context = context;
        this.arrAddress = arrAddress;
    }

    @Override
    public int getCount() {
        return arrAddress.size();
    }

    @Override
    public Object getItem(int i) {
        return arrAddress.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_addressess_result, null);
        }

        TextView tv_addressResults = view.findViewById(R.id.tv_addressResults);
        ImageView iv_booked = view.findViewById(R.id.iv_booked);
        ImageView iv_unBooked = view.findViewById(R.id.iv_unBooked);

        checkIfAddressIsSaved(arrAddress.get(i), iv_booked, iv_unBooked);

        tv_addressResults.setText(arrAddress.get(i).getPlace());

        iv_booked.setOnClickListener(v -> {

            iv_booked.setVisibility(View.GONE);
            iv_unBooked.setVisibility(View.VISIBLE);
            deleteJobInBookmark(arrAddress.get(i));


        });

        iv_unBooked.setOnClickListener(v -> {
            iv_booked.setVisibility(View.VISIBLE);
            iv_unBooked.setVisibility(View.GONE);
            saveJobInBookmark(arrAddress.get(i));

        });


        return view;
    }

    private void deleteJobInBookmark(PlacesInfo arrAddress) {
        String DBname = FirebaseReferences.BOOKMARK_ADDRESS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);
        String address = arrAddress.getPlace();

        Query query = bookmarkDb.orderByChild("addressName").equalTo(address);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Bookmark bookmark = dataSnapshot.getValue(Bookmark.class);
                    if(MY_USER_ID.equalsIgnoreCase(bookmark.getUserId())){
                        FirebaseManager.deleteData(bookmarkDb, dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveJobInBookmark(PlacesInfo arrAddress) {

        String DBname = FirebaseReferences.BOOKMARK_ADDRESS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);
        String address = arrAddress.getPlace();
        Double latitude = arrAddress.getLatitude();
        Double longitude = arrAddress.getLongitude();

        SavedAddress savedAddress = new SavedAddress(MY_USER_ID, address, latitude, longitude, Helper.getCurrentDate());
        FirebaseManager.addData(bookmarkDb, savedAddress);
    }


    private void checkIfAddressIsSaved(PlacesInfo arrAddress, ImageView ivBookmark, ImageView ivNotBookmark) {

        String DBname = FirebaseReferences.BOOKMARK_ADDRESS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);
        String address = arrAddress.getPlace();

        Query query = bookmarkDb.orderByChild("addressName").equalTo(address);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Bookmark bookmark = dataSnapshot.getValue(Bookmark.class);
                    if(MY_USER_ID.equalsIgnoreCase(bookmark.getUserId())){
                        ivBookmark.setVisibility(View.VISIBLE);
                        ivNotBookmark.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
