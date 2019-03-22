package activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;

import models.User;

public class CreateActivity extends AppCompatActivity {

    Button create;
    AppCompatTextView login;
    EditText name, email, password, confirmPass, contact;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(CreateActivity.this, FarmerActivity.class));
            ActivityCompat.finishAffinity(CreateActivity.this);
        }
    }

    private void initViews() {
        create = findViewById(R.id.createBtn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.phone);
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

       /* create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String username = name.getText().toString().trim();
             *//*   String useremail = email.getText().toString().trim();
                String usercontact = contact.getText().toString().trim();
                String userpassword = password.getText().toString().trim();
                String userconfirm = confirmPass.getText().toString().trim();

                if (!username.isEmpty() &&
                        !useremail.isEmpty() &&
                        !userpassword.isEmpty() &&
                        !userconfirm.isEmpty()) {
                    if (
                            userpassword.length() > 6 &&
                                    userpassword.contentEquals(confirmPass.getText())) {
                        signUp(new User(useremail, username, usercontact), userpassword);
                    } else if (userpassword.length() <= 6) {
                        password.setError("Password should be more than 6 characters");
                    } else if (!userpassword.contentEquals(confirmPass.getText())) {
                        password.setError("Passwords doesn't match!");
                    }
                } else {
                    new AlertDialog.Builder(CreateActivity.this)
                            .setTitle("Empty fields")
                            .setMessage("Provide all required credentials")
                            .setCancelable(true);
                }


            });*/
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = name.getText().toString().trim();
                String useremail = email.getText().toString().trim();
                String usercontact = contact.getText().toString().trim();
                String userpassword = password.getText().toString().trim();
                String userconfirm = confirmPass.getText().toString().trim();

                if (!username.isEmpty() &&
                        !useremail.isEmpty() &&
                        !userpassword.isEmpty() &&
                        !userconfirm.isEmpty()) {
                    if (userpassword.equals(userconfirm)) {
                        if (userpassword.length() > 6) {
                            signUp(new User(useremail, username, usercontact), userpassword);
                        } else {
                            Toast.makeText(CreateActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreateActivity.this, "Make sure your passwords match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CreateActivity.this);
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setCancelable(false);
                    builder.setMessage("Empty fields");
                    builder.setMessage("Provide all the required credentials");
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


    private void signUp(final User user, final String password) {

        progressDialog = new ProgressDialog(CreateActivity.this);
        progressDialog.setMessage("Signing up...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateActivity.this, "Signup successfull", Toast.LENGTH_SHORT).show();
                            final FirebaseUser fbUser = mAuth.getCurrentUser();
                            user.setId(fbUser.getUid());
                            db.collection("users")
                                    .document(user.getId())
                                    .set(user)
                                    .addOnCompleteListener(CreateActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(CreateActivity.this, FarmerActivity.class);
                                                startActivity(intent);
                                                ActivityCompat.finishAffinity(CreateActivity.this);
                                            } else {
                                                Toast.makeText(CreateActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                errorDialog();
                                                fbUser.delete();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(CreateActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            errorDialog();
                        }
                    }
                });
        ActivityCompat.finishAffinity(CreateActivity.this);

    }

    private void errorDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Hmmmmm...!");
        builder.setMessage("Something went wrong, please try again");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }


}
