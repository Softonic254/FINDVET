package com.example.user.locvet;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toolbar;

public class vetactivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tab;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vetactivity);
        tab = findViewById(R.id.tab);


        tab = findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("Requests"));
        tab.addTab(tab.newTab().setText("Inbox"));
        tab.addTab(tab.newTab().setText("Clients"));

    }
}