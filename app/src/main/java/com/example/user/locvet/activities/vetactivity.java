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
import com.example.user.locvet.adapters.ViewPagerAdapter;
import com.example.user.locvet.fragments.ClientsFragment;
import com.example.user.locvet.fragments.RequestFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class vetactivity extends AppCompatActivity {

    TabLayout tab;
    ViewPager vetPager;
    FirebaseAuth mAuth;
    EditText contact, email,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vetactivity);
        mAuth = FirebaseAuth.getInstance();

        contact = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        name = findViewById(R.id.vetname);
        tab = findViewById(R.id.vettab);
        vetPager = findViewById(R.id.vetpager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(RequestFragment.newInstance());
        fragments.add(ClientsFragment.newInstance());

        List<CharSequence> titles = new ArrayList<>();
        titles.add("Requests");
        titles.add("Clients");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        vetPager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(vetPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (UserInfo profile : user.getProviderData()) {
                        // Id of the provider (ex: google.com)
                        String providerId = profile.getProviderId();

                        // UID specific to the provider
                        String uid = profile.getUid();

                        // Name, email address, and contact
                        String name = profile.getDisplayName();
                        String email = profile.getEmail();
                        String contact = profile.getPhoneNumber();
                    }
                }return true;
            case R.id.signout:
                mAuth.signOut();
                startActivity(new Intent(vetactivity.this, farmer_login.class));
                ActivityCompat.finishAffinity(vetactivity.this);
                return true;
        }
        return false;
    }

}
