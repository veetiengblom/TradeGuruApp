package com.example.tradeguruapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import com.example.tradeguruapp.trade;
import java.util.ArrayList;
import java.util.List;

public class historyActivity extends AppCompatActivity {

    private tradeDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private ListView historyListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new tradeDatabaseHelper(this);
        database = dbHelper.getReadableDatabase();
        historyListView = findViewById(R.id.historyListView);

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
                String timestampString = cursor.getString(cursor.getColumnIndexOrThrow(tradeDatabaseHelper.COLUMN_TIMESTAMP));

                // Create a Trade object and add it to the trade history list
                tradeHistory.add(new trade(type, companyName, priceDifference, null));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return tradeHistory;
    }

    private void displayTradeHistory(List<trade> tradeHistory) {
        // display the trade history data in UI component
        ArrayAdapter<trade> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tradeHistory);
        historyListView.setAdapter(adapter);
    }
}