package com.example.busyancapstone.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busyancapstone.BusDescription;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Model.Bookmark;
import com.example.busyancapstone.Model.Employee;
import com.example.busyancapstone.Model.Job;
import com.example.busyancapstone.PassengerSearchActivity;
import com.example.busyancapstone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SavedJobAdapter extends RecyclerView.Adapter<SavedJobAdapter.ViewHolder> {

    private DatabaseReference jobDb;
    private List<Bookmark> bookmarkList;
    private Context context;

    public SavedJobAdapter(List<Bookmark> bookmarkList, Context context) {
        this.bookmarkList = bookmarkList;
        this.context = context;
        jobDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.JOBS.getValue());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_jobs_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookmark bookmark = bookmarkList.get(position);
        System.out.println("bookmark id:" + bookmark.getJobId());

        getJobDetails(holder, bookmark);
    }

    private void getJobDetails(ViewHolder viewHolder, Bookmark bookmark) {

        jobDb.child(bookmark.getJobId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Job job = snapshot.getValue(Job.class);
                    viewHolder.jobTitle.setText(job.getTitle());
                    viewHolder.companyName.setText(job.getCompanyName());
                    viewHolder.jobAddress.setText(job.getLocation());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewHolder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusDescription.class);
                intent.putExtra("jobid", bookmark.getJobId());
                startActivity(context, intent, null);
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTitle, companyName, jobAddress, viewBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.tv_jobTitle);
            companyName = itemView.findViewById(R.id.tv_companyName);
            jobAddress = itemView.findViewById(R.id.tv_jobAddress);
            viewBtn = itemView.findViewById(R.id.tv_viewBtn);
        }
    }
}
