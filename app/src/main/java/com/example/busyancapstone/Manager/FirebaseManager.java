package com.example.busyancapstone.Manager;

import android.net.Uri;

import com.example.busyancapstone.Model.RevisionRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.*;

import java.util.Map;

public class FirebaseManager {

    private static final String TAG = "FirebaseManager";


    public static <T> boolean addData(DatabaseReference databaseReference, T data, String key) {

        return databaseReference.child(key).setValue(data).isSuccessful();
    }

    public static <T> boolean addData(DatabaseReference databaseReference, T data) {

        return databaseReference.push().setValue(data).isSuccessful();
    }

    public static boolean updateData(DatabaseReference dbName, String key, Map<String, Object> newData) {

        return dbName.child(key).updateChildren(newData).isSuccessful();
    }

    public static boolean deleteData(DatabaseReference databaseReference, String key) {

        return databaseReference.child(key).removeValue().isSuccessful();
    }

    public static String getMyUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static Uri getProfileImageUri() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
    }

    public static boolean setProfileImageUri(Uri imageUri, String fullName) {

        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(imageUri)
                .setDisplayName(fullName)
                .build();

        return FirebaseAuth.getInstance().getCurrentUser().updateProfile(changeRequest).isSuccessful();
    }

}
