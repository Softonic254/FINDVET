package com.example.user.locvet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateActivity extends AppCompatActivity {

    Button create;
    AppCompatTextView login;
    EditText name, email, password, confirmPass;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        create = findViewById(R.id.createBtn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmpass);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, farmer_login.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString().trim();
                String useremail = email.getText().toString().trim();
                String userpassword = password.getText().toString().trim();
                String userconfirm = confirmPass.getText().toString().trim();

                if (!username.isEmpty() &&
                        !useremail.isEmpty() &&
                        !userpassword.isEmpty() &&
                        !userconfirm.isEmpty()) {
                    if (
                            userpassword.length() > 6 &&
                            userpassword.contentEquals(confirmPass.getText())) {

                        progressDialog = new ProgressDialog(CreateActivity.this);
                        progressDialog.setMessage("Signing up...");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        mAuth.createUserWithEmailAndPassword(useremail, userpassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {


                                            Dialog dialog = new Dialog(CreateActivity.this);
                                            dialog.setTitle("Signup successful!");
                                            dialog.setCancelable(true);
                                            dialog.show();

                                            Intent intent = new Intent(CreateActivity.this,farmer_login.class);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(CreateActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else if (userpassword.length()<=6){
                        password.setError("Password should be more than 6 characters");
                    }else if (!userpassword.contentEquals(confirmPass.getText())){
                        password.setError("Passwords doesn't match!");
                    }
                }else{
                    new AlertDialog.Builder(CreateActivity.this)
                            .setTitle("Empty fields")
                            .setMessage("Provide all required credentials")
                            .setCancelable(true);
                }
            }

            private void updateUI(FirebaseUser currentUser) {
            }
        });
    }


}