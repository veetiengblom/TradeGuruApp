package com.example.tradeguruapp;

/*import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TradeActivity extends AppCompatActivity implements OnTaskComplete {

    TextView companyTextView;
    TextView countdownTextView;
    TextView moneyTextView;
    TextView quoteTextView;
    Button buyBtn;
    Button shortBtn;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");
    LocalDateTime lastRefreshed;
    String date;
    String time;
    String timeOfTrade;
    LocalDateTime dateOfTrade;
    Float buyPrice;
    Float shortPrice;
    Float newPrice;
    Float priceDifference;
    private double userMoney = 1000.0;
    ArrayList<Float> prices = new ArrayList<>();
    ArrayList<Float> times = new ArrayList<>();
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
    LineChart stockLineChart;
    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormat dfStock = new DecimalFormat("0.000");
    Integer adder;
    LocalDate yesterday = LocalDate.now().minusDays(1);
    String checker;
    Integer mentor;
    String mentorName;
    ImageView mImageView;
    String[] mentorNames = {"Bjrön Wahlroos", "Elon Musk", "Jordan Belfort", "WallStreetBets", "Warren Buffett"};
    
    private TradeDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private CountdownHandler countdownHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        companyTextView = (TextView) findViewById(R.id.companyTextView);
        countdownTextView = (TextView) findViewById(R.id.countdownTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        quoteTextView = (TextView) findViewById(R.id.quoteTextView);
        stockLineChart = (LineChart) findViewById(R.id.stockLineChart);
        mImageView = (ImageView) findViewById(R.id.mImageView);
        buyBtn = (Button) findViewById(R.id.buyBtn);
        shortBtn = (Button) findViewById(R.id.shortBtn);
        countdownHandler = new CountdownHandler(this);
        shortBtn.setEnabled(false);
        buyBtn.setEnabled(false);
        new UpdateTask(this, this).execute();
        MentorQuotes mentorQuotes = new MentorQuotes();

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userMoney = preferences.getFloat("money", 1000.0f);
        mentor = preferences.getInt("mentor", -1);

        updateMoneyTextView();

        dbHelper = new TradeDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        mentorName = mentorNames[mentor];
        int mentorImageResource = getMentorImageResource(mentor);
        mImageView.setImageResource(mentorImageResource);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adder < 0) {
                    companyTextView.setText("Stock market has closed for today.");
                    buyBtn.setEnabled(false);
                    shortBtn.setEnabled(false);
                    checker = yesterday.toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dateChecker", (String) checker);
                    editor.apply();
                } else {
                    countdownHandler.cancelCountdown();
                    buyPrice = addDataValue(adder);
                    newPrice = prices.get(adder);
                    timeOfTrade = times.get(adder).toString();
                    if (timeOfTrade.length() < 5) {
                        timeOfTrade = timeOfTrade + "0";
                    }
                    timeOfTrade = timeOfTrade + " " + yesterday;
                    timeOfTrade = timeOfTrade.replace(".", ":");
                    dateOfTrade = LocalDateTime.parse(timeOfTrade, dateTimeFormatter);
                    priceDifference = newPrice - buyPrice;
                    System.out.println("New price:" + newPrice + "\n Old price: " + buyPrice);
                    userMoney += priceDifference;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("money", (float) userMoney);
                    editor.apply();
                    insertTrade("Buy", companyTextView.getText().toString(), priceDifference, dateOfTrade);

                    String randomQuote = mentorQuotes.getRandomQuote(mentorName);
                    quoteTextView.setText(randomQuote);

                    Toast.makeText(TradeActivity.this, "Bought TSLA stock at price: " + buyPrice, Toast.LENGTH_LONG).show();
                    countdownHandler.startCountdown(new CountdownHandler.Callback() {
                        @Override
                        public void onCountdownFinished() {
                            if (priceDifference < 0) {
                                countdownTextView.setText("You lost $: " + dfStock.format(priceDifference));
                            } else {
                                countdownTextView.setText("You profited $: " + dfStock.format(priceDifference));
                            }
                            updateChart(dataVals);
                            adder = adder - 1;
                            editor.putInt("adder", adder);
                            editor.apply();
                            updateMoneyTextView();
                            System.out.println("ADDER: " + adder);
                            quoteTextView.setText("");
                        }
                    });
                }

            }
        });

        shortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adder < 0) {
                    companyTextView.setText("Stock market has closed for today.");
                    buyBtn.setEnabled(false);
                    shortBtn.setEnabled(false);
                    checker = yesterday.toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dateChecker", (String) checker);
                    editor.apply();
                } else {
                    countdownHandler.cancelCountdown();
                    shortPrice = addDataValue(adder);
                    newPrice = prices.get(adder);
                    timeOfTrade = times.get(adder).toString();
                    if (timeOfTrade.length() < 5) {
                        timeOfTrade = timeOfTrade + "0";
                    }
                    timeOfTrade = timeOfTrade + " " + yesterday;
                    timeOfTrade = timeOfTrade.replace(".", ":");
                    dateOfTrade = LocalDateTime.parse(timeOfTrade, dateTimeFormatter);
                    priceDifference = shortPrice -newPrice;
                    userMoney += priceDifference;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("money", (float) userMoney);
                    editor.apply();
                    insertTrade("Short", companyTextView.getText().toString(), priceDifference, dateOfTrade);
                    String randomQuote = mentorQuotes.getRandomQuote(mentorName);
                    quoteTextView.setText(randomQuote);

                    Toast.makeText(TradeActivity.this, "Shorted TSLA stock at price: " + shortPrice, Toast.LENGTH_LONG).show();
                    countdownHandler.startCountdown(new CountdownHandler.Callback() {
                        @Override
                        public void onCountdownFinished() {
                            if (priceDifference < 0) {
                                countdownTextView.setText("You lost $: " + dfStock.format(priceDifference));
                            } else {
                                countdownTextView.setText("You profited $: " + dfStock.format(priceDifference));
                            }
                            updateChart(dataVals);
                            adder = adder - 1;
                            editor.putInt("adder", adder);
                            editor.apply();
                            updateMoneyTextView();
                            System.out.println("ADDER: " + adder);
                            quoteTextView.setText("");
                        }
                    });
                }
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

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        adder = preferences.getInt("adder", 0);
        checker = preferences.getString("dateChecker", "a");
        System.out.println("Yesterday: " + yesterday);
        System.out.println("checker: " + checker);

        if (checker.equals(yesterday + "")) {
            adder = -1;
            companyTextView.setText("Stock market has closed for today.");
            buyBtn.setEnabled(false);
            shortBtn.setEnabled(false);

        } else {

            if (adder == 0) {
                adder = times.size() - 11;
            } else if (adder < 0) {
                adder = times.size() - 11;
            }
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
            System.out.println("Times size: " + times.size());
            System.out.println("Adder: " + adder);
            shortBtn.setEnabled(true);
            buyBtn.setEnabled(true);
        }
    }



    private ArrayList<Entry> dataValues() {
        if (adder == times.size() - 11) {
            Integer index = times.size() - 2;
            for (int i = 0; i < 9; i++) {
                dataVals.add(new Entry(times.get(index), prices.get(index)));
                index = index - 1;
            }
        } else {
            Integer index = adder + 9;
            for (int i = 0; i < 9; i++) {
                dataVals.add(new Entry(times.get(index), prices.get(index)));
                index --;
            }
        }
        return dataVals;
    }

    private Float addDataValue(int i) {
        if (i >= 0) {
            dataVals.add(new Entry(times.get(i), prices.get(i)));
            dataVals.remove(0);
            System.out.println("Time added: " + times.get(i) + "\nPrice added:  " +prices.get(i));
            return prices.get(i + 1);
        } else {
            Toast.makeText(TradeActivity.this, "Stock market has closed", Toast.LENGTH_LONG).show();
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
            String buffer = value + "";
            String minutes = buffer.substring(3,4);
            String hours = buffer.substring(0,1);
            String nextHour = hours + ".00";
            Integer num = Integer.parseInt(minutes);
            if (num > 60) {
                value = Float.parseFloat(nextHour);
            }
            return "" + df.format(value);
        }
    }

    private void updateMoneyTextView() {
            moneyTextView.setText("Balance: $" + String.format("%.2f", userMoney));
    }

    private void insertTrade(String type, String companyName, float priceDifference, LocalDateTime timestamp) {
        ContentValues values = new ContentValues();
        values.put(TradeDatabaseHelper.COLUMN_TYPE, type);
        values.put(TradeDatabaseHelper.COLUMN_COMPANY_NAME, companyName);
        values.put(TradeDatabaseHelper.COLUMN_PRICE_DIFFERENCE, priceDifference);
        values.put(TradeDatabaseHelper.COLUMN_TIMESTAMP, timestamp.toString());
    
        long newRowId = database.insert(TradeDatabaseHelper.TABLE_NAME, null, values);
    
        if (newRowId != -1) {
            System.out.println("Trade record inserted with ID: " + newRowId);
        } else {
            System.out.println("Error inserting trade record");
        }
    }
    private int getMentorImageResource(Integer i) {
        if (i == 0) {
            return R.drawable.bjronvahlroos;
        } else if (i == 1) {
            return R.drawable.elonmusk;
        } else if (i == 2) {
            return R.drawable.jordanbelfort;
        } else if (i == 3) {
            return R.drawable.wallstreetbets;
        } else if (i == 4) {
            return R.drawable.warrenbuffett;
        } else {
            return R.drawable.default_mentor_image;
        }
    }
}*/


import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TradeActivity extends AppCompatActivity implements OnTaskComplete {

    // UI components
    private TextView companyTextView;
    private TextView countdownTextView;
    private TextView moneyTextView;
    private TextView quoteTextView;
    private Button buyBtn;
    private Button shortBtn;

    // Date and Time Formatting
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");

    // Trade-related Date and Time
    private LocalDateTime lastRefreshed;
    private String date;
    private String time;
    private String tradeTime;
    private LocalDateTime tradeDate;

    // Price-related variables
    private Float buyPrice;
    private Float shortPrice;
    private Float newPrice;
    private Float priceDifference;

    // User Account Balance
    private double userMoney = 1000.0;

    // Stock Data Collections
    private ArrayList<Float> stockPrices = new ArrayList<>();
    private ArrayList<Float> tradeTimes = new ArrayList<>();
    private ArrayList<Entry> dataVals = new ArrayList<Entry>();

    // Stock Chart
    private LineChart stockLineChart;

    // Decimal Formats
    private DecimalFormat df = new DecimalFormat("0.00");
    private DecimalFormat dfStock = new DecimalFormat("0.000");

    // Other Trade-related Variables
    private Integer adder;
    private LocalDate previousDay = LocalDate.now().minusDays(1);
    private String dateChecker;

    // Mentor Information
    private Integer selectedMentorIndex;
    private String mentorFullName;
    private ImageView mentorImageView;
    private String[] mentorNames = {"Björn Wahlroos", "Elon Musk", "Jordan Belfort", "WallStreetBets", "Warren Buffett"};

    // Database
    private TradeDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private CountdownHandler countdownHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        initializeViews();
        setupCountdownHandler();
        setupButtonListeners();
        loadUserPreferences();
        setupDatabase();
        new UpdateTask(this, this).execute();
    }

    private void initializeViews() {
        // Initialize UI elements
        companyTextView = findViewById(R.id.companyTextView);
        countdownTextView = findViewById(R.id.countdownTextView);
        moneyTextView = findViewById(R.id.moneyTextView);
        quoteTextView = findViewById(R.id.quoteTextView);
        stockLineChart = findViewById(R.id.stockLineChart);
        mentorImageView = findViewById(R.id.mImageView);
        buyBtn = findViewById(R.id.buyBtn);
        shortBtn = findViewById(R.id.shortBtn);
    }

    private void setupCountdownHandler() {
        // Initialize the CountdownHandler and disable buttons
        countdownHandler = new CountdownHandler(this);
        shortBtn.setEnabled(false);
        buyBtn.setEnabled(false);
    }

    private void setupButtonListeners() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adder < 0) {
                    // Handle the case when the stock market is closed
                    companyTextView.setText("Stock market has closed for today.");
                    buyBtn.setEnabled(false);
                    shortBtn.setEnabled(false);
                    dateChecker = previousDay.toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dateChecker", (String) dateChecker);
                    editor.apply();
                } else {
                    // Handle the case when the stock market is open
                    countdownHandler.cancelCountdown();
                    buyPrice = addDataValue(adder);
                    newPrice = stockPrices.get(adder);
                    tradeTime = tradeTimes.get(adder).toString();
                    if (tradeTime.length() < 5) {
                        tradeTime = tradeTime + "0";
                    }
                    tradeTime = tradeTime + " " + previousDay;
                    tradeTime = tradeTime.replace(".", ":");
                    tradeDate = LocalDateTime.parse(tradeTime, dateTimeFormatter);
                    priceDifference = newPrice - buyPrice;
                    userMoney += priceDifference;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("money", (float) userMoney);
                    editor.apply();
                    insertTrade("Buy", companyTextView.getText().toString(), priceDifference, tradeDate);
                    updateUIAfterBuy();
                    adder = adder - 1;
                    editor.putInt("adder", adder);
                    editor.apply();
                }
            }
        });

        shortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adder < 0) {
                    // Handle the case when the stock market is closed
                    companyTextView.setText("Stock market has closed for today.");
                    buyBtn.setEnabled(false);
                    shortBtn.setEnabled(false);
                    dateChecker = previousDay.toString();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("dateChecker", (String) dateChecker);
                    editor.apply();
                } else {
                    // Handle the case when the stock market is open
                    countdownHandler.cancelCountdown();
                    shortPrice = addDataValue(adder);
                    newPrice = stockPrices.get(adder);
                    tradeTime = tradeTimes.get(adder).toString();
                    if (tradeTime.length() < 5) {
                        tradeTime = tradeTime + "0";
                    }
                    tradeTime = tradeTime + " " + previousDay;
                    tradeTime = tradeTime.replace(".", ":");
                    tradeDate = LocalDateTime.parse(tradeTime, dateTimeFormatter);
                    priceDifference = shortPrice - newPrice;
                    userMoney += priceDifference;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("money", (float) userMoney);
                    editor.apply();
                    insertTrade("Short", companyTextView.getText().toString(), priceDifference, tradeDate);
                    updateUIAfterShort();
                    adder = adder - 1;
                    editor.putInt("adder", adder);
                    editor.apply();
                }
            }
        });
    }

    private void loadUserPreferences() {
        // Load user preferences, such as money and selected mentor
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userMoney = preferences.getFloat("money", 1000.0f);
        selectedMentorIndex = preferences.getInt("mentor", -1);

        moneyTextView.setText("Balance: $" + String.format("%.2f", userMoney));
    }

    private void setupDatabase() {
        // Initialize the database and retrieve mentor information
        dbHelper = new TradeDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        mentorFullName = mentorNames[selectedMentorIndex];
        int mentorImageResource = getMentorImageResource(selectedMentorIndex);
        mentorImageView.setImageResource(mentorImageResource);
        MentorQuotes mentorQuotes = new MentorQuotes();
    }

    @Override
    public void onTaskComplete(JSONObject output) {
        // Callback function when a background task is completed
        parseAndProcessJSONData(output);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        adder = preferences.getInt("adder", 0);
        dateChecker = preferences.getString("dateChecker", "a");

        if (dateChecker.equals(previousDay.toString())) {
            // Handle the case when the market is closed
            adder = -1;
            companyTextView.setText("Stock market has closed for today.");
            buyBtn.setEnabled(false);
            shortBtn.setEnabled(false);
        } else {
            // Handle the case when the market is open
            if (adder == 0 || adder < 0) {
                adder = tradeTimes.size() - 11;
            }
            // Configure and update the stock chart
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
            shortBtn.setEnabled(true);
            buyBtn.setEnabled(true);
        }
    }

    private void parseAndProcessJSONData(JSONObject output) {
        // Parse and process JSON data received from the API
        stockPrices.clear();
        tradeTimes.clear();

        try {
            companyTextView.setText(output.getJSONObject("Meta Data").getString("2. Symbol"));
            date = output.getJSONObject("Meta Data").getString("3. Last Refreshed");
            lastRefreshed = LocalDateTime.parse(date, dateFormatter);
            JSONArray keys = output.getJSONObject("Time Series (1min)").names();
            for (int i = 0; i <= keys.length(); i++) {
                time = date.substring(11, 16);
                time = time.replace(":", ".");
                tradeTimes.add(Float.parseFloat(time));
                stockPrices.add(Float.parseFloat(output.getJSONObject("Time Series (1min)").getJSONObject(date).getString("4. close")));
                lastRefreshed = lastRefreshed.minusMinutes(1);
                date = lastRefreshed.toString();
                date = date + ":00";
                date = date.replace("T", " ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Entry> dataValues() {
        // Generate data values for the chart
        if (adder == tradeTimes.size() - 11) {
            Integer index = tradeTimes.size() - 2;
            for (int i = 0; i < 9; i++) {
                dataVals.add(new Entry(tradeTimes.get(index), stockPrices.get(index)));
                index = index - 1;
            }
        } else {
            Integer index = adder + 9;
            for (int i = 0; i < 9; i++) {
                dataVals.add(new Entry(tradeTimes.get(index), stockPrices.get(index)));
                index--;
            }
        }
        return dataVals;
    }

    private class myXAxisValueFormatter implements IAxisValueFormatter {
        // Custom X-axis value formatter
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            axis.setLabelCount(10, true);
            String buffer = value + "";
            String minutes = buffer.substring(3, 4);
            String hours = buffer.substring(0, 1);
            String nextHour = hours + ".00";
            Integer num = Integer.parseInt(minutes);
            if (num > 60) {
                value = Float.parseFloat(nextHour);
            }
            return "" + df.format(value);
        }
    }

    private Float addDataValue(int i) {
        // Add data value to the chart
        if (i >= 0) {
            dataVals.add(new Entry(tradeTimes.get(i), stockPrices.get(i)));
            dataVals.remove(0);
            System.out.println("Time added: " + tradeTimes.get(i) + "\nPrice added:  " + stockPrices.get(i));
            return stockPrices.get(i + 1);
        } else {
            Toast.makeText(TradeActivity.this, "Stock market has closed", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void updateChart(ArrayList<Entry> dataVals) {
        // Update the chart with new data values
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

    private void updateUIAfterBuy() {
        // Update UI after a Buy operation
        String randomQuote = MentorQuotes.getRandomQuote(mentorFullName);
        quoteTextView.setText(randomQuote);

        Toast.makeText(TradeActivity.this, "Bought TSLA stock at price: " + buyPrice, Toast.LENGTH_LONG).show();
        countdownHandler.startCountdown(new CountdownHandler.Callback() {
            @Override
            public void onCountdownFinished() {
                if (priceDifference < 0) {
                    countdownTextView.setText("You lost $: " + dfStock.format(priceDifference));
                } else {
                    countdownTextView.setText("You profited $: " + dfStock.format(priceDifference));
                }
                updateChart(dataVals);
                updateMoneyTextView();
                System.out.println("ADDER: " + adder);
                quoteTextView.setText("");
            }
        });
    }

    private void updateUIAfterShort() {
        // Update UI after a Short operation
        String randomQuote = MentorQuotes.getRandomQuote(mentorFullName);
        quoteTextView.setText(randomQuote);
        Toast.makeText(TradeActivity.this, "Shorted TSLA stock at price: " + shortPrice, Toast.LENGTH_LONG).show();
        countdownHandler.startCountdown(new CountdownHandler.Callback() {
            @Override
            public void onCountdownFinished() {
                if (priceDifference < 0) {
                    countdownTextView.setText("You lost $: " + dfStock.format(priceDifference));
                } else {
                    countdownTextView.setText("You profited $: " + dfStock.format(priceDifference));
                }
                updateChart(dataVals);
                updateMoneyTextView();
                System.out.println("ADDER: " + adder);
                quoteTextView.setText("");
            }
        });
    }

    private void updateMoneyTextView() {
        // Update the Money TextView with the user's balance
        moneyTextView.setText("Balance: $" + String.format("%.2f", userMoney));
    }

    private void insertTrade(String type, String companyName, float priceDifference, LocalDateTime timestamp) {
        // Insert a trade record into the database
        ContentValues values = new ContentValues();
        values.put(TradeDatabaseHelper.COLUMN_TYPE, type);
        values.put(TradeDatabaseHelper.COLUMN_COMPANY_NAME, companyName);
        values.put(TradeDatabaseHelper.COLUMN_PRICE_DIFFERENCE, priceDifference);
        values.put(TradeDatabaseHelper.COLUMN_TIMESTAMP, timestamp.toString());

        long newRowId = database.insert(TradeDatabaseHelper.TABLE_NAME, null, values);

        if (newRowId != -1) {
            System.out.println("Trade record inserted with ID: " + newRowId);
        } else {
            System.out.println("Error inserting trade record");
        }
    }

    private int getMentorImageResource(Integer i) {
        // Get the resource ID for the mentor's image
        if (i == 0) {
            return R.drawable.bjornwahlroos;
        } else if (i == 1) {
            return R.drawable.elonmusk;
        } else if (i == 2) {
            return R.drawable.jordanbelfort;
        } else if (i == 3) {
            return R.drawable.wallstreetbets;
        } else if (i == 4) {
            return R.drawable.warrenbuffett;
        } else {
            return R.drawable.default_mentor_image;
        }
    }
}
