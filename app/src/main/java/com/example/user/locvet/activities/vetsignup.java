package com.example.user.locvet.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.locvet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.user.locvet.models.User;

public class vetsignup extends AppCompatActivity {


    TextInputEditText name, email, contact, regNo, password, confirmPassword;
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
                final String pass = password.getText().toString().trim();
                final String confirmPass = confirmPassword.getText().toString().trim();


                if (!fullname.isEmpty() &&
                        !emailaddress.isEmpty() &&
                        !number.isEmpty() &&
                        !registrationNo.isEmpty() &&
                        !pass.isEmpty() &&
                        !confirmPass.isEmpty()) {
                    if (pass.length() > 6 &&
                            pass.contentEquals(confirmPass)) {

                        progressDialog = new ProgressDialog(vetsignup.this);
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
                                        signUp(new User(fullname, emailaddress, number, registrationNo), pass);
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
                    errorDialog();
                }
            }
        });

    }

    private void errorDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.setTitle("Empty fields");
        builder.setMessage("Provide all the required credentials");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void signUp(final User user, final String password) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser fbUser = mAuth.getCurrentUser();
                            user.setId(fbUser.getUid());
                            db.collection("vets")
                                    .document(user.getId())
                                    .set(user)
                                    .addOnCompleteListener(vetsignup.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(vetsignup.this, "Signup successfull", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(vetsignup.this, vetactivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(vetsignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                errorDialog();
                                                fbUser.delete();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(vetsignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            errorDialog();
                        }
                    }
                });

    }

}
