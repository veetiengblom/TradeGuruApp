package com.example.tradeguruapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class tradeActivity extends AppCompatActivity implements onTaskComplete {

    TextView companyTextView;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime lastRefreshed;
    String date;
    ArrayList<String> prices = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        companyTextView = (TextView) findViewById(R.id.companyTextView);
        new updateTask(this).execute();
    }

    @Override
    public void onTaskComplete(JSONObject output) {
        try {
            companyTextView.setText(output.getJSONObject("Meta Data").getString("2. Symbol"));
            date = output.getJSONObject("Meta Data").getString("3. Last Refreshed");
            lastRefreshed = LocalDateTime.parse(date, formatter);
            for (int i = 0; i < 10; i++) {
                prices.add(output.getJSONObject("Time Series (1min)").getJSONObject(date).getString("4. close"));
                lastRefreshed = lastRefreshed.minusMinutes(1);
                date = lastRefreshed.toString();
                date = date + ":00";
                date = date.replace("T", " ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}