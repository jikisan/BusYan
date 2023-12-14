package com.example.busyancapstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busyancapstone.Enum.FirebaseReferences;
import com.example.busyancapstone.Helper.Helper;
import com.example.busyancapstone.Model.BusDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private MaterialButton btn_cont;
    private TextView tv_signin;
    private EditText et_name, et_email, et_password, et_confirm, et_mobilenumber;

    private FirebaseAuth mAuth;
    private DatabaseReference busDriverDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        String dbName = FirebaseReferences.BUS_DRIVER.getValue();
        busDriverDb = FirebaseDatabase.getInstance().getReference(dbName);
        mAuth= FirebaseAuth.getInstance();

        et_name = findViewById(R.id.name);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        et_confirm = findViewById(R.id.confirm);
        et_mobilenumber = findViewById(R.id.mobilenumber);

        tv_signin = findViewById(R.id.signin);
        btn_cont = (MaterialButton) findViewById(R.id.cont);

        btn_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openVerificationBus();

                if(allFieldsFilledUp()) {
                    doSignUp();
                }
            }

//            private void openVerificationBus() {
//                startActivity(new Intent(SignUp.this, VerificationBus.class));
//            }

        });

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignUp.this, LoginPageBus.class));
            }

        });


    }

    private boolean allFieldsFilledUp() {

        boolean fullnameIsEmpty = TextUtils.isEmpty(et_name.getText().toString());
        boolean emailIsEmpty = TextUtils.isEmpty(et_email.getText().toString());
        boolean passwordIsEmpty = TextUtils.isEmpty(et_password.getText().toString());
        boolean confirmPasswordIsEmpty = TextUtils.isEmpty(et_confirm.getText().toString());
        boolean mobileNumIsEmpty = TextUtils.isEmpty(et_mobilenumber.getText().toString());

        if( fullnameIsEmpty || emailIsEmpty || passwordIsEmpty || confirmPasswordIsEmpty || mobileNumIsEmpty)
        {
            new Helper(this).showToastLong("Please fill up all fields!");
            return false;
        }

        return true;
    }

    private void doSignUp(){

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()) { savingDataToDatabase(); }

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void savingDataToDatabase(){

        String busDriverId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        String fullName = et_name.getText().toString();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String phoneNum = et_mobilenumber.getText().toString();
        String busCode = "";
        String route = "";
        String imageUrl = "";
        String plateNumber = "";
        boolean isGoogleConnected = false;
        boolean isFacebookConnected = false;

        BusDriver busDriver = new BusDriver(
                busDriverId,
                fullName,
                email,
                phoneNum,
                busCode,
                route,
                imageUrl,
                plateNumber,
                isGoogleConnected,
                isFacebookConnected
        );


        busDriverDb.child(busDriverId).setValue(busDriver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAuth.signOut();
                Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, LoginPageBus.class));

            }
        });
    }

}