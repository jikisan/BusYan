package com.example.busyancapstone;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Enum.NotificationTypes;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Manager.MapsManager;
import com.example.busyancapstone.Model.Application;
import com.example.busyancapstone.Model.Job;
import com.example.busyancapstone.Model.Notifications;
import com.example.busyancapstone.Model.Passenger;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ApplyPage extends AppCompatActivity {

    private static final int REQUEST_CODE_FILE = 1;
    private static final int REQUEST_CODE_LICENSE = 2;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 0;

    private TextView  tv_fullname, tv_email, tv_phoneNum, tv_workExperience,
            etAdditionalInfo, tvAddress;
    private LinearLayout layoutQuestion, layoutResume, layoutExperience, layoutEducation, layoutAddress, layoutLicense;
    private CardView cardQuestion, cardResume, cardExperience, cardEducation, cardAddress, cardLicense;
    private ImageView ivResume, ivLicense, iv_profilePic, iv_uploadPhotoBtn;
    private EditText etEducationalAttainment;
    private Button submit, btnUploadFile, btnUploadLicense, clickedButton;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar circleProgressBar;

    private Double lattitude, longitude;
    private Uri resumeUri, licenseUri, cameraUri;
    private String address, jobId;
    private int x = 0;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Intent> camera;

    private DatabaseReference applicationDb, jobsDb, passengerDb;
    private final String MY_USER_ID = FirebaseManager.getMyUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_page);

        jobId = getIntent().getStringExtra("jobid");

        String applicationDbName = FirebaseReferences.APPLICATIONS.getValue();
        String jobsDbName = FirebaseReferences.JOBS.getValue();
        String passengerDbName = FirebaseReferences.PASSENGER.getValue();

        jobsDb = FirebaseDatabase.getInstance().getReference(jobsDbName);
        passengerDb = FirebaseDatabase.getInstance().getReference(passengerDbName);
        applicationDb = FirebaseDatabase.getInstance().getReference(applicationDbName);

        references();
        requestPermissions();
        setGalleryLaucher();
        getMyDetails();
        clickActions();


    }

    private void getMyDetails() {

        passengerDb.child(MY_USER_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    Passenger passenger = snapshot.getValue(Passenger.class);

                    tv_fullname.setText(passenger.getFullName());
                    tv_email.setText(passenger.getEmail());
                    tv_phoneNum.setText(passenger.getPhoneNum());
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
            }
            else if (item.getItemId() == R.id.passenger_profile) {
                startActivity(new Intent(getApplicationContext(), PassengerProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.passenger_notification) {
                startActivity(new Intent(getApplicationContext(), PassengerNotification.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });

        cardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toggleVisibility(layoutQuestion);
            }
        });

        cardResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutResume);
            }
        });

        cardExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutExperience);
            }
        });

        cardEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutEducation);
            }
        });

        cardAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutAddress);
            }
        });

        cardLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutLicense);
            }
        });

        View.OnClickListener onUploadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the clickedButton variable to determine which ImageView to update later
                clickedButton = (Button) v;
                galleryLauncher.launch("image/*");
            }
        };

        btnUploadFile.setOnClickListener(onUploadClickListener);

        btnUploadLicense.setOnClickListener(onUploadClickListener);

        tv_workExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWorkExperienceDialog();
            }
        });

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutocompleteActivity();
            }
        });

        iv_uploadPhotoBtn.setOnClickListener( view -> {
            openCamera();
        });
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT);

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


    private void setGalleryLaucher() {

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        if (result != null) {
                            // Determine which ImageView to update based on the request code
                            if (clickedButton == btnUploadFile) {
                                resumeUri = result;
                                Helper.loadImage(resumeUri, ivResume);
                            }
                            else if (clickedButton == btnUploadLicense) {
                                licenseUri = result;
                                Helper.loadImage(licenseUri, ivLicense);
                            }
                        }
                    }
                });

        camera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        try {
                            InputStream inputStream =getApplicationContext()
                                    .getContentResolver()
                                    .openInputStream(cameraUri);
                            Bitmap actualImage = BitmapFactory.decodeStream(inputStream);

                            Uri imageUri = Helper.getImageUri(getApplicationContext(), actualImage);
                            Helper.loadImage(imageUri, iv_profilePic);

                        }
                        catch (FileNotFoundException e) {}
                    }
                });
    }

    private void submitApplication() {

        String workExperience = tv_workExperience.getText().toString().trim();
        String educationalAttainment = etEducationalAttainment.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();

        if (!areFieldsValid(workExperience, educationalAttainment, address)) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Sending application...", Toast.LENGTH_SHORT).show();
            circleProgressBar.setVisibility(View.VISIBLE);

            saveDataInDb("", "", workExperience, educationalAttainment, address);
        }
    }

    private void saveDataInDb(String question1, String question2, String workExperience, String educationalAttainment, String address) {

        StorageReference applicationStorage = FirebaseStorage.getInstance().getReference(FirebaseReferences.APPLICATIONS.getValue());

        String resumeName = resumeUri.getLastPathSegment();
        String licenseName = licenseUri.getLastPathSegment();
        String profileName = cameraUri.getLastPathSegment();

        StorageReference resumePicRef = applicationStorage.child(MY_USER_ID).child(resumeName);
        StorageReference licensePicRef = applicationStorage.child(MY_USER_ID).child(licenseName);
        StorageReference profilePicRef = applicationStorage.child(MY_USER_ID).child(profileName);

        resumePicRef.putFile(resumeUri).addOnSuccessListener( task -> {
            resumePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String resumeUrl = uri.toString();

                licensePicRef.putFile(licenseUri).addOnSuccessListener( task1 -> {
                    licensePicRef.getDownloadUrl().addOnSuccessListener( uri1 -> {
                        String licenseUrl = uri1.toString();


                        profilePicRef.putFile(cameraUri).addOnSuccessListener( task2 -> {
                            profilePicRef.getDownloadUrl().addOnSuccessListener( uri2 -> {
                                String profileUrl = uri2.toString();
                                saveDataToDatabase(profileUrl, resumeUrl, licenseUrl, question1, question2, workExperience, educationalAttainment, address);

                            });
                        });

                    });
                });

            });
        });


    }

    private void saveDataToDatabase(String profileUrl, String resumeUrl, String licenseUrl, String question1, String question2, String workExperience, String educationalAttainment, String address){

        Application application = new Application(
                MY_USER_ID,
                jobId,
                profileUrl,
                question1,
                question2,
                resumeUrl,
                workExperience,
                educationalAttainment,
                address,
                longitude,
                lattitude,
                licenseUrl,
                etAdditionalInfo.getText().toString(),
                Helper.getCurrentDate(),
                "pending"
        );

        FirebaseManager.addData(applicationDb, application);

//        applicationDb.push().setValue(application).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Notifications notifications = new Notifications(
//                        liveBus.getBusDriverId(),
//                        relatedNodeId,
//                        getResources().getString(R.string.seatReservationTitle),
//                        getResources().getString(R.string.seatReservationMessage),
//                        Helper.getCurrentDate(),
//                        NotificationTypes.SUBMITTED_APPLICATION
//                );
//
//
//                FirebaseManager.addData(notificationDb, notifications);
//            }
//        });


        circleProgressBar.setVisibility(View.GONE);
        new Helper(this).showToastLong("Application Sent!");
        startActivity(new Intent(this, PassengerJob.class));
    }


    private boolean areFieldsValid(String... fields) {
        for (Object field : fields) {
            if (field instanceof String && ((String) field).isEmpty()) {
                Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (resumeUri == null || licenseUri == null || cameraUri == null) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toggleVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void showWorkExperienceDialog() {
        final String[] workExperienceChoices = {"No work experience", "Below 5 years", "6-11 years", "12-17 years", "18-23 years"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Work Experience");
        builder.setItems(workExperienceChoices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Set the selected choice to the TextView
                tv_workExperience.setText(workExperienceChoices[which]);
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void startAutocompleteActivity() {
        startActivityForResult(MapsManager.searchPlace(this), AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                address = place.getName();
                lattitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
//                String latitude = String.valueOf(place.getLatLng().latitude);
//                String longitude = String.valueOf(place.getLatLng().longitude);

                tvAddress.setText(address);

            }  else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            } else if (resultCode == RESULT_CANCELED) {
            }

        }
        else if (requestCode == REQUEST_CODE_FILE) {
            Uri imageUri = data.getData();

            Picasso.get().load(imageUri)
                    .placeholder(R.drawable.logo)
                    .fit()
                    .centerCrop()
                    .into(ivResume);

        }
        else if (requestCode == REQUEST_CODE_LICENSE) {
            Uri imageUri = data.getData();

            Picasso.get().load(imageUri)
                    .placeholder(R.drawable.logo)
                    .fit()
                    .centerCrop()
                    .into(ivLicense);
        }


    }

    private void requestPermissions() {
        String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Check if the permissions are granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, you can perform your operations here
            } else {
                // Permissions denied
                // You may want to inform the user that the functionality won't work without the required permissions
            }
        }
    }

    private void references() {
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_email = findViewById(R.id.tv_email);
        tv_phoneNum = findViewById(R.id.tv_phoneNum);

        cardQuestion = findViewById(R.id.card_question);

        layoutQuestion = findViewById(R.id.layout_question);

        cardResume = findViewById(R.id.card_resume);

        layoutResume = findViewById(R.id.layout_resume);
        ivResume = findViewById(R.id.iv_resume);
        btnUploadFile = findViewById(R.id.btn_uploadFile);

        cardExperience = findViewById(R.id.card_experience);

        layoutExperience = findViewById(R.id.layout_experience);

        cardEducation = findViewById(R.id.card_education);

        layoutEducation = findViewById(R.id.layout_education);
        etEducationalAttainment = findViewById(R.id.et_educationalAttainment);

        cardAddress = findViewById(R.id.card_address);

        layoutAddress = findViewById(R.id.layout_address);
        tvAddress = findViewById(R.id.tv_address);

        cardLicense = findViewById(R.id.card_license);

        layoutLicense = findViewById(R.id.layout_license);
        ivLicense = findViewById(R.id.iv_license);
        btnUploadLicense = findViewById(R.id.btn_uploadLicense);

        etAdditionalInfo = findViewById(R.id.et_additionalInfo);
        tv_workExperience = findViewById(R.id.tv_workExperience);
        submit = findViewById(R.id.submit);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        iv_profilePic = findViewById(R.id.iv_profilePic);
        iv_uploadPhotoBtn = findViewById(R.id.iv_uploadPhotoBtn);

        circleProgressBar = findViewById(R.id.progressBar);

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            Places.initialize(getApplicationContext(), apiKey);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
}