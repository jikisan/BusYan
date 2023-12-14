package com.example.busyancapstone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Bookmark;
import com.example.busyancapstone.Model.TempJob;
import com.example.busyancapstone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomAdapterJobs extends BaseAdapter {

    private Context context;
    private List<TempJob> tempJobs;
    private OnItemClickListener onItemClickListener;
    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    public interface OnItemClickListener {
        void onItemClick(String key);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public CustomAdapterJobs(Context context, List<TempJob> tempJobs) {
        this.context = context;
        this.tempJobs = tempJobs;
    }

    @Override
    public int getCount() {
        return tempJobs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_jobs, null);
        }

        TextView title = convertView.findViewById(R.id.tv_title);
        TextView company = convertView.findViewById(R.id.tv_company);
        TextView location = convertView.findViewById(R.id.tv_location);
        TextView salary = convertView.findViewById(R.id.tv_salary);
        TextView postDate = convertView.findViewById(R.id.tv_postDate);
        ImageView bookmark = convertView.findViewById(R.id.iv_bookmark);
        ImageView notBookmark = convertView.findViewById(R.id.iv_notBookmark);

        title.setText(tempJobs.get(position).getJob().getTitle());
        company.setText(tempJobs.get(position).getJob().getCompany());
        location.setText(tempJobs.get(position).getJob().getLocation());
        salary.setText(tempJobs.get(position).getJob().getSalary());
        postDate.setText(tempJobs.get(position).getJob().getPostDate());
        checkIfBookmarked(tempJobs.get(position), bookmark, notBookmark);

        bookmark.setOnClickListener(v -> {

            bookmark.setVisibility(View.GONE);
            notBookmark.setVisibility(View.VISIBLE);
            deleteJobInBookmark(tempJobs.get(position));


        });

        notBookmark.setOnClickListener(v -> {
            bookmark.setVisibility(View.VISIBLE);
            notBookmark.setVisibility(View.GONE);
            saveJobInBookmark(tempJobs.get(position));

        });

        convertView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getKey(position));
            }
        });

        return convertView;
    }


    private void deleteJobInBookmark(TempJob tempJob) {
        String DBname = FirebaseReferences.BOOKMARK_JOBS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);
        String jobId = tempJob.getId();


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

    private void saveJobInBookmark(TempJob tempJob) {

        String DBname = FirebaseReferences.BOOKMARK_JOBS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);
        String jobId = tempJob.getId();

        Bookmark bookmark = new Bookmark(MY_USER_ID, jobId, Helper.getCurrentDate());
        FirebaseManager.addData(bookmarkDb, bookmark);
    }

    private void checkIfBookmarked(TempJob tempJob, ImageView ivBookmark, ImageView ivNotBookmark) {

        String DBname = FirebaseReferences.BOOKMARK_JOBS.getValue();
        DatabaseReference bookmarkDb = FirebaseDatabase.getInstance().getReference(DBname);
        String jobId = tempJob.getId();

        Query query = bookmarkDb.orderByChild("jobId").equalTo(jobId);
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

    private String getKey(int position) {
        // Assuming you have a method in your Job class to get the key
        return tempJobs.get(position).getId();
    }

    public void updateList(List<TempJob> newJobs) {
        this.tempJobs.clear();
        this.tempJobs.addAll(newJobs);
    }
}
