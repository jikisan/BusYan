package com.example.busyancapstone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Passenger;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class PassengerEditProfile extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private MaterialButton update;
    private ImageView iv_profilePic, iv_uploadPhotoBtn;
    private EditText et_fullName, et_busCode, et_route, et_email, et_mobileNum;
    private SwitchCompat sw_googleSwitch;

    private Uri cameraUri, imageUri;
    private String currentImageUrl;
    private int x = 0;
    private static final int PERMISSION_REQUEST_CODE = 0;

    private DatabaseReference passengerDb;
    private StorageReference passengerStorage;
    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_edit_profile);

        String dbName = FirebaseReferences.PASSENGER.getValue();
        passengerDb = FirebaseDatabase.getInstance().getReference(dbName);

        reference();
        requestPermissions();
        getData();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.passenger_profile) {

                return true;
            } else if (item.getItemId() == R.id.passenger_home) {
                startActivity(new Intent(getApplicationContext(), PassengerActivity.class));
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();

            }

        });

        ActivityResultLauncher<Intent> camera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                try {
                    InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(cameraUri);
                    Bitmap actualImage = BitmapFactory.decodeStream(inputStream);

                    imageUri = Helper.getImageUri(getApplicationContext(), actualImage);
                    Helper.loadImage(imageUri, iv_profilePic);

                } catch (FileNotFoundException e) {
                }
            }
        });

        iv_uploadPhotoBtn.setOnClickListener(view -> {

            openCamera(camera);

        });

    }

    private void openCamera(ActivityResultLauncher<Intent> camera) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "Image" + x);
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Image from Camera App");
        cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        x++;

        if (cameraUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            intent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT);

            camera.launch(intent);
        }
    }

    private void updateProfile() {

        if (imageUri != null) {
            System.out.println("with image");
            saveImageUrlToStorage(imageUri);
        } else {
            System.out.println("no image");
            saveDataToDatabase(currentImageUrl);
        }

    }

    private void requestPermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Check if the permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permissions already granted
            // You can perform your operations here
        } else {
            // Request permissions
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, you can perform your operations here
            } else {
                // Permissions denied
                // You may want to inform the user that the functionality won't work without the required permissions
            }
        }
    }

    private void getData() {

        passengerDb.child(MY_USER_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Passenger passenger = snapshot.getValue(Passenger.class);

//                    Helper.loadImage(FirebaseManager.getProfileImageUri(), iv_profilePic);

                    currentImageUrl = passenger.getImageUrl();
                    Picasso.get()
                            .load(passenger.getImageUrl())
                            .placeholder(R.drawable.passengerpic)
                            .into(iv_profilePic);

                    et_fullName.setText(passenger.getFullName());
                    et_email.setText(passenger.getEmail());
                    et_mobileNum.setText(passenger.getPhoneNum());

                    if (passenger.isGoogleConnected()) {
                        sw_googleSwitch.setChecked(true);
                    } else {
                        sw_googleSwitch.setChecked(false);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void reference() {

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        update = (MaterialButton) findViewById(R.id.update);

        iv_profilePic = findViewById(R.id.iv_profilePic);
        iv_uploadPhotoBtn = findViewById(R.id.iv_uploadPhotoBtn);
        et_fullName = findViewById(R.id.name);
        et_email = findViewById(R.id.email);
        et_mobileNum = findViewById(R.id.number);
        sw_googleSwitch = findViewById(R.id.sw_googleSwitch);

        bottomNavigationView.setSelectedItemId(R.id.passenger_profile);

    }

    private void saveImageUrlToStorage(Uri photoUri) {


        passengerStorage = FirebaseStorage.getInstance().getReference(FirebaseReferences.PASSENGER.getValue());

        String newImageName = photoUri.getLastPathSegment();
        StorageReference userPicRef = passengerStorage.child(MY_USER_ID).child(newImageName);

        userPicRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        userPicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                saveDataToDatabase(imageUrl);
                            }
                        });
                    }
                });

    }

    private void saveDataToDatabase(String imageUrl) {

        String fullName = et_fullName.getText().toString();
        String phoneNum = et_mobileNum.getText().toString();

        HashMap<String, Object> newData = new HashMap<>();
        newData.put("fullName", fullName);
        newData.put("phoneNum", phoneNum);
        newData.put("imageUrl", imageUrl);

        passengerDb.child(MY_USER_ID).updateChildren(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                new Helper(PassengerEditProfile.this).showToastLong("Profile updated ddd");
                startActivity(new Intent(PassengerEditProfile.this, PassengerProfile.class));

            }
        });

//        FirebaseManager.setProfileImageUri(imageUri, fullName);
//        FirebaseManager.updateData(passengerDb, MY_USER_ID, newData);

    }

}