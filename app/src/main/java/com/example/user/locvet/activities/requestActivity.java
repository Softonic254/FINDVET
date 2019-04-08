package com.example.user.locvet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.user.locvet.R;

public class requestActivity extends AppCompatActivity {

    EditText request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        request = findViewById(R.id.request);
    }

}
