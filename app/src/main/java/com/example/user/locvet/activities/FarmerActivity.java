package com.example.user.locvet.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.user.locvet.R;
import com.example.user.locvet.adapters.ViewPagerAdapter;
import com.example.user.locvet.fragments.InboxFragment;
import com.example.user.locvet.fragments.VetsFragment;
import com.example.user.locvet.models.Requests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FarmerActivity extends AppCompatActivity {

    TabLayout tab;
    PopupWindow popupWindow;
    ViewPager farmerPager;
    FirebaseAuth mAuth;
    EditText contact, email;

    private FirebaseFirestore db;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmeractivity);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        contact = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        tab = findViewById(R.id.tab);
        farmerPager = findViewById(R.id.farmerpager);
        popupWindow = new PopupWindow();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(InboxFragment.newInstance());
        fragments.add(VetsFragment.newInstance());

        List<CharSequence> titles = new ArrayList<>();
        titles.add("Notifications");
        titles.add("Vets");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        farmerPager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(farmerPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                // code
                return true;
            case R.id.request:
                request();
                return true;
            case R.id.myrequest:

                return true;
            case R.id.signout:
                mAuth.signOut();
                startActivity(new Intent(FarmerActivity.this, farmer_login.class));
                ActivityCompat.finishAffinity(FarmerActivity.this);
                return true;
        }
        return false;
    }

    private void request() {
        LinearLayout linearLayout = new LinearLayout(FarmerActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(16, 16, 16, 16);
        linearLayout.setDividerPadding(16);

        final EditText request = new EditText(FarmerActivity.this);
        request.setHint("Request");
        request.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final EditText location = new EditText(FarmerActivity.this);
        location.setHint("Location");
        location.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearLayout.addView(request);
        linearLayout.addView(location);

        AlertDialog dialog = new AlertDialog.Builder(FarmerActivity.this)
                .setTitle("Add a new request")
                .setView(linearLayout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String requestString = request.getText().toString().trim();
                        String locationString = location.getText().toString().trim();

                        if (!requestString.isEmpty() && !locationString.isEmpty()) {
                            db.collection("requests")
                                    .add(new Requests(mAuth.getCurrentUser().getUid(), requestString, locationString, false))
                                    .addOnCompleteListener(FarmerActivity.this, new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(FarmerActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.w(TAG, task.getException());
                                                Toast.makeText(FarmerActivity.this, "Failed, please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}