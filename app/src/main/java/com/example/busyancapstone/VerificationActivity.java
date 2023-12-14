package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Model.Passenger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private final String TAG = "TagVerificationActivity";

    private MaterialButton verifybtn;
    private PinView otp;
    private LinearLayout layoutTimer;
    private TextView tvTimer, tvResendCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private String verificationId;
//    private String resendingToken;
    private String fullName;
    private String email;
    private String password;
    private String phoneNum;
    private String imageUrl;
    private boolean isGoogleConnected;
    private boolean isResend;;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 60 seconds
    private boolean timerRunning;

    private FirebaseAuth mAuth;
    private DatabaseReference passengerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        verifybtn = findViewById(R.id.verifybtn);
        otp = findViewById(R.id.otp);
        tvTimer = findViewById(R.id.tvTimer);
        tvResendCode = findViewById(R.id.tvResendCode);
        layoutTimer = findViewById(R.id.layoutTimer);

        String dbName = FirebaseReferences.PASSENGER.getValue();
        passengerDb = FirebaseDatabase.getInstance().getReference(dbName);
        mAuth= FirebaseAuth.getInstance();

        retrieveDataFromIntent();
        startTimer();


        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = otp.getText().toString();

                if(code.length() == 6){

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
                else {
                    Toast.makeText(VerificationActivity.this, "Please enter the 6-digit code.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        tvResendCode.setOnClickListener( view -> {

            Toast.makeText(this, "OTP code has been sent!", Toast.LENGTH_SHORT).show();
            sendOtp(phoneNum, true);
            layoutTimer.setVisibility(View.VISIBLE);
            tvResendCode.setVisibility(View.GONE);
            timeLeftInMillis = 60000;
            startTimer();
        });
    }

    private void retrieveDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
//            verificationId = intent.getStringExtra("verificationId");
//            resendingToken = intent.getStringExtra("resendingToken");
            fullName = intent.getStringExtra("fullName");
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            phoneNum = intent.getStringExtra("phoneNum");
            imageUrl = intent.getStringExtra("imageUrl");
            isGoogleConnected = intent.getBooleanExtra("isGoogleConnected", false);

            sendOtp(phoneNum, false);
            Toast.makeText(this, "OTP code sent!", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendOtp(String phoneNumber, boolean isResend) {

        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);

                                Log.d(TAG, "onCodeSent:" + s);
                                verificationId = s;
                                resendingToken = forceResendingToken;

                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d(TAG, "onVerificationFailed: " + "Phone Number Verification Failed: " + e.getMessage());
                                Toast.makeText(VerificationActivity.this, "Phone Number Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        });


        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
//        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        Log.d(TAG, "signInWithPhoneAuthCredential: ");

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    String phoneId = task.getResult().getUser().getUid();
                    if (task.isSuccessful()) {
                        mAuth.getInstance().getCurrentUser().delete();

                        FirebaseUser currentUser = task.getResult().getUser();
                        createUserWithEmailPassword();
                    }
                });
    }

    private void createUserWithEmailPassword(){

        Log.d(TAG, "createUserWithEmailPassword: " + email + " " + password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmailAndPassword: Complete!");

                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmailAndPassword: Success!");

                            AuthCredential emailCredential = EmailAuthProvider.getCredential(email, password);
                            signInUser(emailCredential);


                        }
                    }
                });


    }

    private void signInUser(AuthCredential credential) {

        Log.d(TAG, "signInUser: ");

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) { savingDataToDatabase(); }

            }
        });
    }

    private void savingDataToDatabase(){

        Log.d(TAG, "savingDataToDatabase: ");
        String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Passenger passenger = new Passenger(
                                passengerId,
                                fullName,
                                email,
                                phoneNum,
                                imageUrl,
                                isGoogleConnected
                        );

                        passengerDb.child(passengerId).setValue(passenger).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                mAuth.signOut();
                                Toast.makeText(VerificationActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(VerificationActivity.this, LoginPage.class));
                            }
                    });
                }
            });

    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the previous timer if running
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timeLeftInMillis = 60000;
                layoutTimer.setVisibility(View.GONE);
                tvResendCode.setVisibility(View.VISIBLE);
            }
        }.start();

        timerRunning = true;
        updateCountdownText();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
            updateCountdownText();
        }
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);

    }
}