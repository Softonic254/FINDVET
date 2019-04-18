package com.example.user.locvet.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.locvet.R;
import com.example.user.locvet.models.Vets;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class vetsignup extends AppCompatActivity {


    TextInputEditText name, email, contact, regNo, county, password, confirmPassword;
    AppCompatButton signup;
    AppCompatButton login;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vetsignup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        name = findViewById(R.id.vetname);
        email = findViewById(R.id.vetemail);
        contact = findViewById(R.id.vetContact);
        regNo = findViewById(R.id.regNo);
        password = findViewById(R.id.vetpassword);
        confirmPassword = findViewById(R.id.vetconfirmpass);
        signup = findViewById(R.id.vetsignupBtn);
        login = findViewById(R.id.vetlogin);
        county = findViewById(R.id.countyarea);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vetsignup.this, vetLogin.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname = name.getText().toString().trim();
                final String emailaddress = email.getText().toString().trim();
                final String number = contact.getText().toString().trim();
                final String registrationNo = regNo.getText().toString().trim();
                final String areaCounty = county.getText().toString().trim();
                final String pass = password.getText().toString().trim();
                final String confirmPass = confirmPassword.getText().toString().trim();


                if (!fullname.isEmpty() &&
                        !emailaddress.isEmpty() &&
                        !number.isEmpty() &&
                        !registrationNo.isEmpty() &&
                        !areaCounty.isEmpty() &&
                        !pass.isEmpty() &&
                        !confirmPass.isEmpty()) {
                    if (pass.length() > 6 &&
                            pass.contentEquals(confirmPass)) {

                        new ProgressDialog(vetsignup.this);
                        progressDialog.setMessage("Signing up...");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        CollectionReference vets = db.collection("registered-vets");
                        Query query = vets.whereEqualTo("registration_number", registrationNo);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    if (task.getResult().size() != 0) {
                                        signUp(new Vets(fullname, emailaddress, number, registrationNo, areaCounty), pass);
                                    } else {
                                        Log.d(TAG, "No such vet found");
                                    }
                                } else {
                                    Log.d(TAG, "Query failed");
                                }
                            }
                        });
                    } else if (pass.length() < 6) {
                        password.setError("Password too short");
                    } else if (!pass.contentEquals(confirmPass)) {
                        confirmPassword.setError("Passwords should match!");
                    }
                } else {
                    Toast.makeText(vetsignup.this, "Please provide all credentials required.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void signUp(final Vets vet, final String password) {
        mAuth.createUserWithEmailAndPassword(vet.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser fbUser = mAuth.getCurrentUser();
                            vet.setId(fbUser.getUid());
                            db.collection("vets")
                                    .document(vet.getId())
                                    .set(vet)
                                    .addOnCompleteListener(vetsignup.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(vetsignup.this, "Signup successfull", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(vetsignup.this, vetactivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(vetsignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                fbUser.delete();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(vetsignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}
