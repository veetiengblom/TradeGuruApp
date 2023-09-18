package com.example.tradeguruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tradeBtn = (Button) findViewById(R.id.tradeBtn);
        Button historyBtn = (Button) findViewById(R.id.historyBtn);
        Button settingsBtn = (Button) findViewById(R.id.settingsBtn);

        tradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTradeActivity();
            }
        });
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHistoryActivity();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });
    }

    public void openTradeActivity(){
        Intent intent = new Intent(this, tradeActivity.class);
        startActivity(intent);
    }
    public void openHistoryActivity(){
        Intent intent = new Intent(this, historyActivity.class);
        startActivity(intent);
    }
    public void openSettingsActivity(){
        Intent intent = new Intent(this, settingsActivity.class);
        startActivity(intent);
    }
}