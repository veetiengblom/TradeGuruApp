package com.example.tradeguruapp;

import android.os.Bundle;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class tradeActivity extends AppCompatActivity implements onTaskComplete {

    TextView companyTextView;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime lastRefreshed;
    String date;
    String time;
    ArrayList<Float> prices = new ArrayList<>();
    ArrayList<Float> times = new ArrayList<>();
    LineChart stockLineChart;
    DecimalFormat df = new DecimalFormat("0.00");
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        companyTextView = (TextView) findViewById(R.id.companyTextView);
        stockLineChart = (LineChart) findViewById(R.id.stockLineChart);
        new updateTask(this).execute();
    }

    @Override
    public void onTaskComplete(JSONObject output) {

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
    

}