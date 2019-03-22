package activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.locvet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import models.User;

public class farmer_login extends AppCompatActivity {

    Button login;
    AppCompatTextView createAcc, vetlog;
    EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_login);
        mAuth = FirebaseAuth.getInstance();
        initViews();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(farmer_login.this, FarmerActivity.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(farmer_login.this);
        }
    }

    private void initViews() {
        login = findViewById(R.id.log);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        createAcc = findViewById(R.id.createAccount);
        vetlog = findViewById(R.id.vetLogin);

        vetlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(farmer_login.this, vetLogin.class);
                startActivity(intent);
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(farmer_login.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (!userEmail.isEmpty() &&
                        !userPassword.isEmpty()) {
                    logIn(userEmail, userPassword);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(farmer_login.this);
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


    private void logIn(String email, String password) {
        final ProgressDialog dialog = new ProgressDialog(farmer_login.this);
        dialog.setMessage("Signing in...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(farmer_login.this, FarmerActivity.class);
                            startActivity(intent);
                            ActivityCompat.finishAffinity(farmer_login.this);
                        } else
                            Toast.makeText(farmer_login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}