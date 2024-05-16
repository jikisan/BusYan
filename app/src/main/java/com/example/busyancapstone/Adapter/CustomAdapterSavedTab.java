package com.example.busyancapstone.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.busyancapstone.SavedJobFragment;
import com.example.busyancapstone.SavedPlaceFragment;

public class CustomAdapterSavedTab extends FragmentStateAdapter {

    public CustomAdapterSavedTab(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new SavedPlaceFragment();
            default:
                return new SavedJobFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
