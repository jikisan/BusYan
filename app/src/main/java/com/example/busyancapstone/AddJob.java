package com.example.busyancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Job;
import com.example.busyancapstone.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddJob extends AppCompatActivity {

    private EditText et_title, et_company, et_desc, et_jobType, et_loc,
            et_salary, et_postDate;
    private Button btn_save;
    private DatabaseReference jobDb;
    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        String dbName = FirebaseReferences.JOBS.getValue();
        jobDb = FirebaseDatabase.getInstance().getReference(dbName);

        references();

        btn_save.setOnClickListener( view -> {

            saveJob();
        });
    }

    private void references() {

        et_title = findViewById(R.id.et_title);
        et_company = findViewById(R.id.et_company);
        et_desc = findViewById(R.id.et_desc);
        et_jobType = findViewById(R.id.et_jobType);
        et_loc = findViewById(R.id.et_loc);
        et_salary = findViewById(R.id.et_salary);
        et_postDate = findViewById(R.id.et_postDate);
        btn_save = findViewById(R.id.btn_save);
    }

    private void saveJob(){

        String title = et_title.getText().toString();
        String company = et_company.getText().toString();
        String desc = et_desc.getText().toString();
        String jobType = et_jobType.getText().toString();
        String loc = et_loc.getText().toString();
        String salary = et_salary.getText().toString();
        String postDate = et_postDate.getText().toString();

        Job job = new Job(
                title,
                company,
                desc,
                jobType,
                loc,
                salary,
                postDate
        );



        FirebaseManager.addData(jobDb, job);
        Toast.makeText(this, "Job Created!", Toast.LENGTH_SHORT).show();
    }


}