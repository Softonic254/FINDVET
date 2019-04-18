package com.example.user.locvet.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.locvet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class vetLogin extends AppCompatActivity {

    EditText vetemail, vetpassword;
    AppCompatTextView vetsignup;
    AppCompatButton vetlogin,reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vet_login);
        mAuth = FirebaseAuth.getInstance();

        initViews();

    }


    private void initViews() {
        vetemail = findViewById(R.id.emailvet);
        vetpassword = findViewById(R.id.passwordvet);
        vetsignup = findViewById(R.id.createVet);
        vetlogin = findViewById(R.id.loginVet);
        reset = findViewById(R.id.forgotpass);

        vetsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vetLogin.this, vetsignup.class);
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(vetLogin.this,VetPassReset.class));
            }
        });

        vetlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = vetemail.getText().toString().trim();
                String userPassword = vetpassword.getText().toString().trim();

                if (!userEmail.isEmpty() &&
                        !userPassword.isEmpty()) {
                    logIn(userEmail, userPassword);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(vetLogin.this);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setCancelable(false);
                    builder.setTitle("Empty fields");
                    builder.setMessage("Provide both email and password");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            }
        });
    }

    private void logIn(String vetemail, String vetpassword) {
        final ProgressDialog dialog = new ProgressDialog(vetLogin.this);
        dialog.setMessage("Signing in...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth.signInWithEmailAndPassword(vetemail, vetpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(vetLogin.this, vetactivity.class);
                            startActivity(intent);
                            ActivityCompat.finishAffinity(vetLogin.this);
                        } else
                            Toast.makeText(vetLogin.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
