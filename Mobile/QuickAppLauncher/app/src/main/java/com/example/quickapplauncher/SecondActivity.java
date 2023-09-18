package com.example.quickapplauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (getIntent().hasExtra("com.example.quickapplauncher.SOMETHING")) {
            TextView intentTextView = (TextView) findViewById(R.id.intentTextView);
            String text = getIntent().getExtras().getString("com.example.quickapplauncher.SOMETHING");
            intentTextView.setText(text);
        }
    }
}