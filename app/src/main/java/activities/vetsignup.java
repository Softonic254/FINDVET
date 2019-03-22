package com.example.user.locvet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class vetsignup extends AppCompatActivity {


    TextInputEditText name, email, contact, regNo, password, confirmPassword;
    AppCompatButton signup;
    AppCompatTextView login;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vetsignup);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

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
                String fullname = name.getText().toString().trim();
                String emailaddress = email.getText().toString().trim();
                String number = contact.getText().toString().trim();
                String registrationNo = regNo.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();


                if (!fullname.isEmpty() &&
                        !emailaddress.isEmpty() &&
                        !number.isEmpty() &&
                        !registrationNo.isEmpty() &&
                        !pass.isEmpty() &&
                        confirmPass.isEmpty()) {
                    if (pass.length() > 6 &&
                            pass.contentEquals(confirmPass)) {
                        progressDialog = new ProgressDialog(vetsignup.this);
                        progressDialog.setTitle("Signing up");
                        progressDialog.setMessage("Signing up...");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        mAuth.createUserWithEmailAndPassword(emailaddress,pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser().getEmail();
                                            mAuth.getCurrentUser().getDisplayName();
                                            mAuth.getCurrentUser().getPhoneNumber();

                                            Dialog dialog = new Dialog(vetsignup.this);
                                            dialog.setTitle("Signup successful!");
                                            dialog.setCancelable(false);
                                            dialog.show();

                                            Intent intent = new Intent(vetsignup.this,vetactivity.class);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(vetsignup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else if (pass.length() <= 6) {
                        password.setError("Password must be more than 6 characters");
                    } else if (!pass.contentEquals(confirmPass)) {
                        confirmPassword.setError("Passwords does not match!");
                    }
                } else {
                    alertDialog();
                }
            }
        });

    }

    private void alertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Empty fields!");
        builder.setMessage("Provide all the required fields for successful registration");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
    }
}
