package com.example.user.locvet;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Switch;
import android.widget.Toolbar;

public class FarmerActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tab;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmeractivity);

        tab = findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("Notifications"));
        tab.addTab(tab.newTab().setText("Inbox"));
        tab.addTab(tab.newTab().setText("Vets"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }
}
