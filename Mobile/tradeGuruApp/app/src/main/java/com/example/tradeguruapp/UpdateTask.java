package com.example.tradeguruapp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UpdateTask extends AsyncTask<String, String, JSONObject> {

    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=1min&apikey=DV0ZUWVK94S3TOCY";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime lastRefreshed;
    String date;
    String time;
    String price;
    String timePrice;
    String company;
    String fileName;

    private OnTaskComplete listener;
    private Context context;

    public UpdateTask(OnTaskComplete listener, Context context){
        this.listener=listener;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();
            System.out.println("\n sending 'get' request to URL:" + url);
            System.out.println("Response code:" + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject myResponse = new JSONObject(response.toString());
            /*
            date = myResponse.getJSONObject("Meta Data").getString("3. Last Refreshed");
            lastRefreshed = LocalDateTime.parse(date, formatter);

            company = myResponse.getJSONObject("Meta Data").getString("2. Symbol");
            fileName = "stockData.txt";
            File path = context.getApplicationContext().getFilesDir();
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(company.getBytes());
            for (int i = 0; i < 20; i++) {
                time = date.substring(11, 16);
                time = time.replace(":", ".");
                price = myResponse.getJSONObject("Time Series (1min)").getJSONObject(date).getString("4. close");
                timePrice = time + ";" + price;
                writer.write(timePrice.getBytes());
                lastRefreshed = lastRefreshed.minusMinutes(1);
                date = lastRefreshed.toString();
                date = date + ":00";
                date = date.replace("T", " ");
            }
            writer.close();

             */
            return myResponse;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(JSONObject result) {
        listener.onTaskComplete(result);
    }
}