package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Manager.SendSmsTask;
import com.example.busyancapstone.Model.Passenger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpPassenger extends AppCompatActivity {

    private final String TAG = "TAGSignUpPassenger";

    private MaterialButton cont;
    private TextView signin;
    private TextInputEditText editTextFullName, editTextEmail, editTextPassword, editTextConfirm,
            editTextMobileNum;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private FirebaseAuth mAuth;
    private DatabaseReference passengerDb;

//    private final PhoneAuthProvider
//            .OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider
//            .OnVerificationStateChangedCallbacks(){
//
//        @Override
//        public void onCodeSent(@NonNull String verificationId,
//                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
//            // The SMS verification code has been sent to the provided phone number, we
//            // now need to ask the user to enter the code and then construct a credential
//            // by combining the code with a verification ID.
//            Log.d(TAG, "onCodeSent:" + verificationId);
//
//            // Save verification ID and resending token so we can use them later
//            navigateToCodeInputActivity(verificationId);
//
//        }
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//
//
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Log.d(TAG, "onVerificationFailed: " + "Phone Number Verification Failed: " + e.getMessage());
//            Toast.makeText(SignUpPassenger.this, "Phone Number Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_passenger);

        String dbName = FirebaseReferences.PASSENGER.getValue();
        passengerDb = FirebaseDatabase.getInstance().getReference(dbName);
        mAuth= FirebaseAuth.getInstance();

        editTextFullName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirm = findViewById(R.id.confirm);
        editTextMobileNum = findViewById(R.id.mobilenumber);
        signin = findViewById(R.id.signin);

        cont = (MaterialButton) findViewById(R.id.cont);

        clickActions();
    }

    private void clickActions() {
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validation()) {
                    String phoneNum = editTextMobileNum.getText().toString();
                    navigateToCodeInputActivity(phoneNum);
//                    sendOtp(phoneNum);

                }

            }

//            private void openVerificationActivity() {
//                Intent intent = new Intent(SignUpPassenger.this, VerificationActivity.class);
//                startActivity(intent);
//            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLoginpage();

            }

            private void openLoginpage() {
                Intent intent = new Intent(SignUpPassenger.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private boolean validation() {

        boolean fullnameIsEmpty = TextUtils.isEmpty(editTextFullName.getText().toString());
        boolean emailIsEmpty = TextUtils.isEmpty(editTextEmail.getText().toString());
        boolean passwordIsEmpty = TextUtils.isEmpty(editTextPassword.getText().toString());
        boolean confirmPasswordIsEmpty = TextUtils.isEmpty(editTextConfirm.getText().toString());
        boolean mobileNumIsEmpty = TextUtils.isEmpty(editTextMobileNum.getText().toString());

        String password = editTextPassword.getText().toString();
        String confirmPass = editTextConfirm.getText().toString();
        String phoneNum = editTextMobileNum.getText().toString();

        if( fullnameIsEmpty || emailIsEmpty || passwordIsEmpty || confirmPasswordIsEmpty || mobileNumIsEmpty){
            new Helper(this).showToastLong("Please fill up all fields!");
            return false;
        }
        else if(!password.equals(confirmPass)){
            new Helper(this).showToastLong("Password does not match!");
            return false;
        }
//        else if(phoneNum.length() != 13){
//            new Helper(this).showToastLong("Not valid phone number!");
//            return false;
//        }

        return true;
    }



    private void sendOtp(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);

                                Log.d(TAG, "onCodeSent:" + verificationId);
                                resendingToken = forceResendingToken;
                                navigateToCodeInputActivity(verificationId);

                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d(TAG, "onVerificationFailed: " + "Phone Number Verification Failed: " + e.getMessage());
                                Toast.makeText(SignUpPassenger.this, "Phone Number Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void navigateToCodeInputActivity(String verificationId) {

        String fullName = editTextFullName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phoneNum = editTextMobileNum.getText().toString();
        String imageUrl = "";
        String password = editTextPassword.getText().toString();
        boolean isGoogleConnected = false;

        Intent intent = new Intent(this, VerificationActivity.class);
//        intent.putExtra("verificationId", verificationId);
//        intent.putExtra("resendingToken", resendingToken);
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("isGoogleConnected", isGoogleConnected);

        startActivity(intent);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Define the regex pattern for a Philippine mobile number
        String regex = "^(09|\\+639|639)\\d{9,11}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(phoneNumber);

        // Return true if the phone number matches the pattern, false otherwise
        return matcher.matches();
    }

//    private void doSignUp(){
//
//        String email = editTextEmail.getText().toString();
//        String password = editTextPassword.getText().toString();
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                if(task.isSuccessful()) { savingDataToDatabase(); }
//
//                            }
//                        });
//                    }
//                });
//    }
//
//    private void savingDataToDatabase(){
//
//
//        String passengerId =   FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String fullName = editTextFullName.getText().toString();
//        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        String phoneNum = editTextMobileNum.getText().toString();
//        String imageUrl = "";
//        boolean isGoogleConnected = false;
//
//        Passenger passenger = new Passenger(
//                passengerId,
//                fullName,
//                email,
//                phoneNum,
//                imageUrl,
//                isGoogleConnected
//        );
//
//        passengerDb.child(passengerId).setValue(passenger).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                startActivity(new Intent(SignUpPassenger.this, LoginPageBus.class));
//                Toast.makeText(SignUpPassenger.this, "Account Created", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}