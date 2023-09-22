package com.example.tradeguruapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class tradeActivity extends AppCompatActivity implements onTaskComplete {

    TextView companyTextView;
    TextView countdownTextView;
    TextView moneyTextView;
    Button buyBtn;
    Button shortBtn;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime lastRefreshed;
    String date;
    String time;
    Float buyPrice;
    Float shortPrice;
    Float priceDifference;
    private double userMoney = 1000.0;
    ArrayList<Float> prices = new ArrayList<>();
    ArrayList<Float> times = new ArrayList<>();
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
    LineChart stockLineChart;
    DecimalFormat df = new DecimalFormat("0.00");
    Integer adder;

    private CountdownHandler countdownHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        companyTextView = (TextView) findViewById(R.id.companyTextView);
        countdownTextView = (TextView) findViewById(R.id.countdownTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        stockLineChart = (LineChart) findViewById(R.id.stockLineChart);
        buyBtn = (Button) findViewById(R.id.buyBtn);
        shortBtn = (Button) findViewById(R.id.shortBtn);
        countdownHandler = new CountdownHandler(this);
        new updateTask(this, this).execute();

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userMoney = preferences.getFloat("money", 1000.0f);

        updateMoneyTextView();

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownHandler.cancelCountdown();
                buyPrice = addDataValue(adder);
                priceDifference = buyPrice - prices.get(adder);
                userMoney += priceDifference;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("money", (float) userMoney);
                editor.apply();
                
                Toast.makeText(tradeActivity.this, "Bought TSLA stock at price: " + buyPrice, Toast.LENGTH_LONG).show();
                countdownHandler.startCountdown(new CountdownHandler.Callback() {
                    @Override
                    public void onCountdownFinished() {
                        countdownTextView.setText("Your profit/loss $: " + priceDifference);
                        updateChart(dataVals);
                        adder = adder - 1;
                        updateMoneyTextView();
                    }
                });
            }
        });

        shortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownHandler.cancelCountdown();
                shortPrice = addDataValue(adder);
                priceDifference = shortPrice - prices.get(adder);
                userMoney += priceDifference;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("money", (float) userMoney);
                editor.apply();

                Toast.makeText(tradeActivity.this, "Shorted TSLA stock at price: " + shortPrice, Toast.LENGTH_LONG).show();
                countdownHandler.startCountdown(new CountdownHandler.Callback() {
                    @Override
                    public void onCountdownFinished() {
                        countdownTextView.setText("Your profit/loss $: " + priceDifference);
                        updateChart(dataVals);
                        adder = adder - 1;
                        updateMoneyTextView();
                    }
                });
            }
        });
    }

    @Override
    public void onTaskComplete(JSONObject output) {

        prices.clear();
        times.clear();

        try {
            companyTextView.setText(output.getJSONObject("Meta Data").getString("2. Symbol"));
            date = output.getJSONObject("Meta Data").getString("3. Last Refreshed");
            lastRefreshed = LocalDateTime.parse(date, formatter);
            JSONArray keys = output.getJSONObject("Time Series (1min)").names();
            for (int i = 0; i <= keys.length(); i++) {
                time = date.substring(11, 16);
                time = time.replace(":", ".");
                times.add(Float.parseFloat(time));
                prices.add(Float.parseFloat(output.getJSONObject("Time Series (1min)").getJSONObject(date).getString("4. close")));
                lastRefreshed = lastRefreshed.minusMinutes(1);
                date = lastRefreshed.toString();
                date = date + ":00";
                date = date.replace("T", " ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adder = times.size() - 11;

        LineDataSet lineDataSet = new LineDataSet(dataValues(), "");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        lineDataSet.setLineWidth(4);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setDrawValues(true);

        Legend legend = stockLineChart.getLegend();
        legend.setForm(Legend.LegendForm.EMPTY);

        LineData data = new LineData(dataSets);

        XAxis xAxis = stockLineChart.getXAxis();
        YAxis yAxisRight = stockLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        xAxis.setValueFormatter(new myXAxisValueFormatter());

        Description description = new Description();
        description.setText("");
        stockLineChart.setNoDataText("Loading... Or Maybe No Data Found?");
        stockLineChart.setDescription(description);
        stockLineChart.setDrawGridBackground(true);
        stockLineChart.setDrawBorders(true);
        stockLineChart.setData(data);
        stockLineChart.invalidate();
    }



    private ArrayList<Entry> dataValues() {
        Integer index = times.size() - 2;
        for (int i = 0; i < 9; i++) {
            dataVals.add(new Entry(times.get(index), prices.get(index)));
            index = index - 1;
        }
        return dataVals;
    }

    private Float addDataValue(int i) {
        if (times.get(i) != null) {
            dataVals.add(new Entry(times.get(i), prices.get(i)));
            dataVals.remove(0);
            return prices.get(i + 1);
        } else {
            Toast.makeText(tradeActivity.this, "Stock market has closed", Toast.LENGTH_LONG).show();
        } return null;
    }

    private void updateChart(ArrayList<Entry> dataVals) {
        LineDataSet lineDataSet = new LineDataSet(dataVals, "");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineDataSet.setLineWidth(4);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setDrawValues(true);
        stockLineChart.setData(data);
        stockLineChart.invalidate();
    }

    private class myXAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            axis.setLabelCount(10, true);
            return "" + df.format(value);
        }
    }

    private void updateMoneyTextView() {
            moneyTextView.setText("Money: $" + String.format("%.2f", userMoney));
    }
}
