package com.example.tradeguruapp;

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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class tradeActivity extends AppCompatActivity implements onTaskComplete {

    TextView companyTextView;
    TextView moneyTextView;
    Button buyBtn;
    Button shortBtn;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime lastRefreshed;
    String price;
    String time;
    Float buyPrice;
    Float shortPrice;
    String playerMoney;
    ArrayList<Float> prices = new ArrayList<>();
    ArrayList<Float> times = new ArrayList<>();
    LineChart stockLineChart;
    DecimalFormat df = new DecimalFormat("0.00");
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        companyTextView = (TextView) findViewById(R.id.companyTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        stockLineChart = (LineChart) findViewById(R.id.stockLineChart);
        buyBtn = (Button) findViewById(R.id.buyBtn);
        shortBtn = (Button) findViewById(R.id.shortBtn);
        new updateTask(this, this).execute();
        /*
        try {
            playerMoney = getMoneyAmount();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        System.out.println("player money: " + playerMoney);
        moneyTextView.setText(playerMoney);


        //Timer timer = new Timer();
        //timer.schedule(new quora(), 0, 60000);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyPrice = prices.get(0);
                Toast.makeText(tradeActivity.this, "Bought TSLA stock at price: " + buyPrice, Toast.LENGTH_LONG).show();


            }
        });

        shortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shortPrice = prices.get(0);
                Toast.makeText(tradeActivity.this, "Shorted TSLA stock at price: " + shortPrice, Toast.LENGTH_LONG).show();

            }
        });
    }
    /*
    class quora extends TimerTask {
        public void run() {
            new updateTask(tradeActivity.this).execute();
        }
    }
     */

    @Override
    public void onTaskComplete (JSONObject output){
        /*
        prices.clear();
        times.clear();

        try {
            companyTextView.setText(output.getJSONObject("Meta Data").getString("2. Symbol"));
            date = output.getJSONObject("Meta Data").getString("3. Last Refreshed");
            lastRefreshed = LocalDateTime.parse(date, formatter);
            for (int i = 0; i < 10; i++) {
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

         */
        readFromFile();
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

    private ArrayList<Entry> dataValues () {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(times.get(9), prices.get(9)));
        dataVals.add(new Entry(times.get(8), prices.get(8)));
        dataVals.add(new Entry(times.get(7), prices.get(7)));
        dataVals.add(new Entry(times.get(6), prices.get(6)));
        dataVals.add(new Entry(times.get(5), prices.get(5)));
        dataVals.add(new Entry(times.get(4), prices.get(4)));
        dataVals.add(new Entry(times.get(3), prices.get(3)));
        dataVals.add(new Entry(times.get(2), prices.get(2)));
        dataVals.add(new Entry(times.get(1), prices.get(1)));
        dataVals.add(new Entry(times.get(0), prices.get(0)));
        return dataVals;
    }

    private class myXAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            axis.setLabelCount(10, true);
            return "" + df.format(value);
        }
    }
    private String getMoneyAmount() throws IOException {
        String money;
        InputStream is = getAssets().open("com/example/tradeguruapp/assets/money.txt");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        money = new String(buffer);
        System.out.println("Money?? " + money);
        return money;
    }

    private void readFromFile() {
        
        try (BufferedReader br = new BufferedReader(new FileReader("stockData.txt"))) {
            String line;
            line = br.readLine();
            if (line != null) {
                companyTextView.setText(line);
            }
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                time = parts[0];
                price = parts[1];
                times.add(Float.parseFloat(time));
                prices.add(Float.parseFloat(price));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}