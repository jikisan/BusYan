package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Manager.FirebaseManager;
import com.example.busyancapstone.Model.Passenger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginPage extends AppCompatActivity {

    private final String TAG = "TAGLoginPage";

    private MaterialButton loginbtn;
    private TextView signup, forgot;
    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;


    //   sign in with gmail
    private FirebaseDatabase database;
    private Button googlBtn;
    private DatabaseReference passengerDb;

    private GoogleSignInClient mGoogleSignInClient;

    private int RC_SIGN_IN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        passengerDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.PASSENGER.getValue());

        references();
        clickActions();



    }

    private void clickActions() {
        googlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignIn();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                passengerDb
                        .orderByChild("email")
                        .equalTo(email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                    regularSignIn(email, password);
                                }
                                else{
                                    Toast.makeText(LoginPage.this, getResources().getString(R.string.signInError), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginPage.this, getResources().getString(R.string.signInError), Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSignUpPassenger();

            }

            private void openSignUpPassenger() {
                Intent intent = new Intent(LoginPage.this, SignUpPassenger.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openForgotPasswordActivity();

            }

            private void openForgotPasswordActivity() {
                Intent intent = new Intent(LoginPage.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void googleSignIn() {

        Log.d(TAG, "googleSignIn: ");
        GoogleSignInOptions gso = new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(LoginPage.this, gso);


        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);

    }

    private void regularSignIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginPage.this, "Successfully login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PassengerActivity.class);
                            startActivity(intent);

                        }

                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Login Failed!!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: ");

        if (requestCode == RC_SIGN_IN){

            Log.d(TAG, "requestCode: " + RC_SIGN_IN);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account);

            }
            catch (ApiException e) {
                Log.d(TAG, "Exception requestCode: " + RC_SIGN_IN);
                Log.d(TAG, "Exception onActivityResult: " + e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void firebaseAuth(GoogleSignInAccount account ) {

        Log.d(TAG, "firebaseAuth: ");

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d(TAG, "signInWithCredential: Complete!");
                if (task.isSuccessful()){

                    Log.d(TAG, "signInWithCredential: Success!");
                    FirebaseUser user = mAuth.getCurrentUser();

                    Passenger passenger = new Passenger(
                            user.getUid(),
                            user.getDisplayName(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.getPhotoUrl().toString(),
                            true
                    );

                    FirebaseManager.addData(passengerDb, passenger, user.getUid());

                    Intent intent = new Intent(LoginPage.this,PassengerActivity.class);
                    startActivity(intent);

                }
                else {

                    Toast.makeText(LoginPage.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void references() {

        googlBtn = findViewById(R.id.btn);
        database = FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();

        loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signup);
        forgot = findViewById(R.id.forgot);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

    }


//    private void savingDataToDatabase(){
//
//        Log.d(TAG, "savingDataToDatabase: ");
//        String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(fullName)
//                .build();
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//
//                        Passenger passenger = new Passenger(
//                                passengerId,
//                                fullName,
//                                email,
//                                phoneNum,
//                                imageUrl,
//                                isGoogleConnected
//                        );
//
//                        passengerDb.child(passengerId).setValue(passenger).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                mAuth.signOut();
//                                Toast.makeText(VerificationActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
//                                finish();
//                                startActivity(new Intent(VerificationActivity.this, LoginPage.class));
//                            }
//                        });
//                    }
//                });
//
//    }

}