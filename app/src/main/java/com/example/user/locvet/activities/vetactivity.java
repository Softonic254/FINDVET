package com.example.user.locvet.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;

import com.example.user.locvet.R;
import com.google.firebase.auth.FirebaseAuth;

public class vetactivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tab;
    PagerAdapter pagerAdapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vetactivity);
        tab = findViewById(R.id.tab);
        mAuth = FirebaseAuth.getInstance();


        tab = findViewById(R.id.vettab);
        tab.addTab(tab.newTab().setText("Requests"));
        tab.addTab(tab.newTab().setText("Inbox"));
        tab.addTab(tab.newTab().setText("Clients"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

}