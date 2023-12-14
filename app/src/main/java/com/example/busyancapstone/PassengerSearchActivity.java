package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.busyancapstone.Adapter.CustomAdapterJobs;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Model.BusDriver;
import com.example.busyancapstone.Model.Job;
import com.example.busyancapstone.Model.TempJob;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PassengerSearchActivity extends AppCompatActivity{

    private BottomNavigationView bottomNavigationView;
    private SearchView search;
    private ListView jobListview;

    private List<Job> arrJobs = new ArrayList<>();
    private List<TempJob> arrTempJobs = new ArrayList<>();
    private DatabaseReference jobsDb;
    private CustomAdapterJobs customAdapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_search);

        String dbName = FirebaseReferences.JOBS.getValue();
        jobsDb = FirebaseDatabase.getInstance().getReference(dbName);
        category = getIntent().getStringExtra("category");

        references();

        customAdapter = new CustomAdapterJobs(this, arrTempJobs);

        jobListview.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

        clickActions();
        getJobs();

    }

    private void getJobs() {

        jobsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrTempJobs.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Job job = dataSnapshot.getValue(Job.class);
                    TempJob tempJob = new TempJob(dataSnapshot.getKey(), job);
                    arrTempJobs.add(tempJob);
                }

                customAdapter.notifyDataSetChanged();

                if(category != null) {
                    search.setQuery(category, false);
                    search.onActionViewExpanded();
                    search.setQuery(category, false);
                    search.clearFocus();

                    search(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void clickActions() {
        bottomNavigationView.setSelectedItemId(R.id.passenger_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.passenger_home) {

                return true;
            } else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.passenger_notification) {
                startActivity(new Intent(getApplicationContext(), PassengerNotification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

        customAdapter.setOnItemClickListener(new CustomAdapterJobs.OnItemClickListener() {
            @Override
            public void onItemClick(String key) {

                Intent intent = new Intent(PassengerSearchActivity.this, BusDescription.class);
                intent.putExtra("jobid", key);
                startActivity(intent);
            }
        });

    }

    private void search(String newText) {

        ArrayList<TempJob> tempArrJobs = new ArrayList<>();

        if (newText.isEmpty()) {
            getJobs();
        }
        else {

            tempArrJobs.clear();

            for (TempJob tempJob : arrTempJobs) {

                String jobTitle = tempJob.getJob().getTitle().toLowerCase();

                if (jobTitle.contains(newText.toLowerCase())) {
                    tempArrJobs.add(tempJob);
                }
            }

            customAdapter.updateList(tempArrJobs);
            customAdapter.notifyDataSetChanged();
        }

    }

    private void references(){
        search = findViewById(R.id.search);
        jobListview = findViewById(R.id.jobListview);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }

}