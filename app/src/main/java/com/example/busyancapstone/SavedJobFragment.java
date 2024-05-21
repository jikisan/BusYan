package com.example.busyancapstone;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.busyancapstone.Adapter.SavedJobAdapter;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Bookmark;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedJobFragment extends Fragment {

    private final String My_USER_ID = FirebaseManager.getMyUserId();

    private RecyclerView rv_savedJobs;
    private List<Bookmark> bookmarkList = new ArrayList<>();
    private DatabaseReference bookmarkDb;
    private SavedJobAdapter savedJobAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_job, container, false);
        rv_savedJobs = view.findViewById(R.id.recyclerview_savedJobs);

        bookmarkDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BOOKMARK_JOBS.getValue());
        savedJobAdapter = new SavedJobAdapter(bookmarkList, getContext());
        rv_savedJobs.setAdapter(savedJobAdapter);
        rv_savedJobs.setLayoutManager(new LinearLayoutManager(getContext()));

        bookmarkDb.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bookmarkList.clear();

                System.out.println("Snapshot:");
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bookmark bookmark = data.getValue(Bookmark.class);
                    if (bookmark.getUserId().equalsIgnoreCase(My_USER_ID)) {
                        bookmarkList.add(bookmark);
                    }
                }

                savedJobAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rv_savedJobs.setAdapter(savedJobAdapter);

        return view;
    }
}