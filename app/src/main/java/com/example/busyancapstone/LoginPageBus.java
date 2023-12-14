package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginPageBus extends AppCompatActivity {

    private MaterialButton loginbtn;
    private TextView signup;
    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference busDriverDb;

    //    sign with gmail
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private Button googleAuth;

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_bus);


        busDriverDb = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BUS_DRIVER.getValue());
        //      google sign in
        googleAuth = findViewById(R.id.btn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.gcm_defaultSenderId)).requestEmail().build();

        googleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignIn();
            }
        });

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        mAuth= FirebaseAuth.getInstance();

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        // admin and admin
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                busDriverDb
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            signIn(email, password);
                        }
                        else{
                            Toast.makeText(LoginPageBus.this, getResources().getString(R.string.signInError), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginPageBus.this, getResources().getString(R.string.signInError), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        TextView signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSignUp();

            }

            private void openSignUp() {
                Intent intent = new Intent(LoginPageBus.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

    private void googleSignIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    private void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginPageBus.this, "Successfully login",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), BusStartingPage.class);
                            startActivity(intent);

                        }

                        else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginPageBus.this, "Login Failed!!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());

            }
            catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }


        }

    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser user = auth.getCurrentUser();

                    HashMap<String,Object> map = new HashMap<>();
                    map.put("id",user.getUid());
                    map.put("name", user.getDisplayName());
                    map.put("profile", user.getPhotoUrl());

                    database.getReference().child("users").child(user.getUid()).setValue(map);

                    Intent intent = new Intent(LoginPageBus.this,BusStartingPage.class);
                    startActivity(intent);

                }
                else {

                    Toast.makeText(LoginPageBus.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


}