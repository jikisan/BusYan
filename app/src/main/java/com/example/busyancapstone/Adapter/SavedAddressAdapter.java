package com.example.busyancapstone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busyancapstone.Model.Bookmark;
import com.example.busyancapstone.Model.SavedAddress;
import com.example.busyancapstone.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class SavedAddressAdapter extends RecyclerView.Adapter<SavedAddressAdapter.ViewHolder>  {

    private List<SavedAddress> savedAddresses;
    private Context context;

    public SavedAddressAdapter(List<SavedAddress> savedAddresses, Context context) {
        this.savedAddresses = savedAddresses;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_address_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedAddressAdapter.ViewHolder holder, int position) {
        SavedAddress address = savedAddresses.get(position);

        holder.addressName.setText(address.getAddressName());
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "View", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedAddresses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView addressName, viewBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addressName = itemView.findViewById(R.id.tv_addressName);
            viewBtn = itemView.findViewById(R.id.tv_viewBtn);
        }
    }
}
