package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Bookmark;
import com.example.busyancapstone.Model.Job;
import com.example.busyancapstone.Model.TempJob;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BusDescription extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextView textJobDescription;
    private TextView textAboutCompany;
    private BottomNavigationView bottomNavigationView;

    private TextView buses;
    private TextView tvTitle;
    private TextView tvCompany;
    private TextView tvLocation;
    private TextView tvSalary;
    private TextView tvPostDate;
    private MaterialButton btn_apply;
    private MaterialButton btn_save;
    private MaterialButton btn_notSaved;

    private DatabaseReference jobsDb;
    private String jobId;

    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_description);

        jobId = getIntent().getStringExtra("jobid");

        String dbName = FirebaseReferences.JOBS.getValue();
        jobsDb = FirebaseDatabase.getInstance().getReference(dbName);

        references();
        getJobDetails();
        checkIfSaved();
        setTabLayout();
        clickActions();

    }

    private void checkIfSaved() {

        String DBname = FirebaseReferences.BOOKMARK_JOBS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);

        Query query = bookmarkDb.orderByChild("jobId").equalTo(jobId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Bookmark bookmark = dataSnapshot.getValue(Bookmark.class);
                    if(MY_USER_ID.equalsIgnoreCase(bookmark.getUserId())){
                        btn_save.setVisibility(View.VISIBLE);
                        btn_notSaved.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getJobDetails() {

        jobsDb.child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    Job job = snapshot.getValue(Job.class);

                    tvTitle.setText(job.getTitle());
                    tvCompany.setText(job.getCompany());
                    tvLocation.setText(job.getLocation());
                    tvSalary.setText(job.getSalary());
                    tvPostDate.setText(job.getPostDate());
                    textJobDescription.setText(job.getDescription());
                    buses.setText(job.getJobType());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickActions() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    textJobDescription.setVisibility(View.VISIBLE);
                    textAboutCompany.setVisibility(View.GONE);
                } else if (position == 1) {
                    textJobDescription.setVisibility(View.GONE);
                    textAboutCompany.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Implement if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Implement if needed
            }
        });

        // Your existing code for handling BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.passenger_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.passenger_home) {
                // Handle the home action if needed
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

        btn_save.setOnClickListener( v -> {
            btn_save.setVisibility(View.GONE);
            btn_notSaved.setVisibility(View.VISIBLE);
            deleteJobInBookmark();
        });

        btn_notSaved.setOnClickListener( v -> {
            btn_save.setVisibility(View.VISIBLE);
            btn_notSaved.setVisibility(View.GONE);
            saveJobInBookmark();
        });

        btn_apply.setOnClickListener( v -> {

            Intent intent = new Intent(BusDescription.this, ApplyPage.class);
            intent.putExtra("jobid", jobId);
            startActivity(intent);
        });
    }

    private void saveJobInBookmark() {

        String DBname = FirebaseReferences.BOOKMARK_JOBS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);

        Bookmark bookmark = new Bookmark(MY_USER_ID, jobId, Helper.getCurrentDate());
        FirebaseManager.addData(bookmarkDb, bookmark);


    }

    private void deleteJobInBookmark() {
        String DBname = FirebaseReferences.BOOKMARK_JOBS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);

        Query query = bookmarkDb.orderByChild("jobId").equalTo(jobId);
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

    private void setTabLayout() {
        // Your existing code for handling TabLayout and TextViews
        tabLayout.addTab(tabLayout.newTab().setText("Job Description"));
        tabLayout.addTab(tabLayout.newTab().setText("About Company"));

    }

    private void references() {
        // Initialize UI elements
        tabLayout = findViewById(R.id.tabLayout);
        textJobDescription = findViewById(R.id.textJobDescription);
        textAboutCompany = findViewById(R.id.textAboutCompany);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        buses = findViewById(R.id.buses);
        tvTitle = findViewById(R.id.tv_title);
        tvCompany = findViewById(R.id.tv_company);
        tvLocation = findViewById(R.id.tv_location);
        tvSalary = findViewById(R.id.tv_salary);
        tvPostDate = findViewById(R.id.tv_postDate);
        btn_apply = findViewById(R.id.btn_apply);
        btn_notSaved = findViewById(R.id.btn_notSaved);
        btn_save = findViewById(R.id.btn_save);
    }


}