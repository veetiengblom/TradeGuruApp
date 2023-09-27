package com.example.tradeguruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.example.tradeguruapp.trade;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class historyActivity extends AppCompatActivity {

    private tradeDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private ListView tradeHistoryListView;
    private TextView winPercentageTextView;
    private TextView moneyMadeTextView;
    private double wins = 0;
    private double losses = 0;
    private String winPercentage;
    private double getter;
    private double userMoney;
    DecimalFormat formatter = new DecimalFormat("0.00");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new tradeDatabaseHelper(this);
        tradeActivity tradeActivity = new tradeActivity();
        database = dbHelper.getReadableDatabase();
        tradeHistoryListView = findViewById(R.id.tradeHistoryListView);
        winPercentageTextView = findViewById(R.id.winPercentageTextView);
        moneyMadeTextView = findViewById(R.id.moneyMadeTextView);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        getter = preferences.getFloat("money", 0.0f);
        if (getter == 0.0) {
            userMoney = 0;
        } else {
            userMoney = Double.parseDouble(formatter.format(getter - 1000));
        }

        // Retrieve trade history data
        List<trade> tradeHistory = getTradeHistory();

        // Display trade history data in a ListView or any other UI component
        displayTradeHistory(tradeHistory);
    }

    private List<trade> getTradeHistory() {
        List<trade> tradeHistory = new ArrayList<>();

        String[] projection = {
                tradeDatabaseHelper.COLUMN_TYPE,
                tradeDatabaseHelper.COLUMN_COMPANY_NAME,
                tradeDatabaseHelper.COLUMN_PRICE_DIFFERENCE,
                tradeDatabaseHelper.COLUMN_TIMESTAMP
        };

        Cursor cursor = database.query(
                tradeDatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndexOrThrow(tradeDatabaseHelper.COLUMN_TYPE));
                String companyName = cursor.getString(cursor.getColumnIndexOrThrow(tradeDatabaseHelper.COLUMN_COMPANY_NAME));
                float priceDifference = cursor.getFloat(cursor.getColumnIndexOrThrow(tradeDatabaseHelper.COLUMN_PRICE_DIFFERENCE));
                String timestampString = cursor.getString(cursor.getColumnIndexOrThrow((tradeDatabaseHelper.COLUMN_TIMESTAMP)));
                if (priceDifference < 0) {
                    losses ++;
                } else {
                    wins ++;
                }

                // Create a Trade object and add it to the trade history list
                tradeHistory.add(0, new trade(type, companyName, priceDifference, LocalDateTime.parse(timestampString)));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return tradeHistory;
    }

    private void displayTradeHistory(List<trade> tradeHistory) {
        // display the trade history data in UI component
        tradeAdapter adapter = new tradeAdapter(this, tradeHistory);
        tradeHistoryListView.setAdapter(adapter);
        winPercentage = formatter.format((wins / (wins + losses)) * 100);
        if (userMoney < 0) {
            moneyMadeTextView.setText("Lost: " + userMoney + "$");
        } else if (userMoney > 0) {
            moneyMadeTextView.setText("Won: " + userMoney + "$");
        } else {
            moneyMadeTextView.setText("Congrats u have made: " + userMoney + "$");
        }

        winPercentageTextView.setText("Win Percentage: " + winPercentage + "%");
    }
}