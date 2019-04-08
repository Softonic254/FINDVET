package com.example.user.locvet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.user.locvet.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import com.example.user.locvet.adapters.ViewPagerAdapter;
import com.example.user.locvet.fragments.InboxFragment;
import com.example.user.locvet.fragments.NotificationFragment;
import com.example.user.locvet.fragments.VetsFragment;

public class FarmerActivity extends AppCompatActivity {

    TabLayout tab;
    ViewPager farmerPager;
    FirebaseAuth mAuth;
    EditText contact, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmeractivity);
        mAuth = FirebaseAuth.getInstance();

        contact = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        tab = findViewById(R.id.tab);
        farmerPager = findViewById(R.id.farmerpager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(NotificationFragment.newInstance());
        fragments.add(InboxFragment.newInstance());
        fragments.add(VetsFragment.newInstance());

        List<CharSequence> titles = new ArrayList<>();
        titles.add("Notifications");
        titles.add("Inbox");
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
                Intent intent = new Intent(FarmerActivity.this,requestActivity.class);
                startActivity(intent);
                return true;
            case R.id.signout:
                mAuth.signOut();
                startActivity(new Intent(FarmerActivity.this, farmer_login.class));
                ActivityCompat.finishAffinity(FarmerActivity.this);
                return true;
        }
        return false;
    }

}
