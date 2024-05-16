package com.example.busyancapstone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.busyancapstone.Adapter.SavedAddressAdapter;
import com.example.busyancapstone.Adapter.SavedJobAdapter;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Bookmark;
import com.example.busyancapstone.Model.SavedAddress;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedPlaceFragment extends Fragment {

    private final String My_USER_ID = FirebaseManager.getMyUserId();

    private RecyclerView rv_savedPlaces;
    private List<SavedAddress> saveAddresses = new ArrayList<>();
    private DatabaseReference bookmarkAddressDb;
    private SavedAddressAdapter savedAddressAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_place, container, false);
        rv_savedPlaces = view.findViewById(R.id.recyclerview_savedPlaces);

        bookmarkAddressDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BOOKMARK_ADDRESS.getValue());
        savedAddressAdapter = new SavedAddressAdapter(saveAddresses, getContext());
        rv_savedPlaces.setAdapter(savedAddressAdapter);
        rv_savedPlaces.setLayoutManager(new LinearLayoutManager(getContext()));

        bookmarkAddressDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                saveAddresses.clear();
                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    SavedAddress savedAddress = addressSnapshot.getValue(SavedAddress.class);
                    if (savedAddress.getUserId().equalsIgnoreCase(My_USER_ID)) {
                        saveAddresses.add(savedAddress);
                    }
                }
                savedAddressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}