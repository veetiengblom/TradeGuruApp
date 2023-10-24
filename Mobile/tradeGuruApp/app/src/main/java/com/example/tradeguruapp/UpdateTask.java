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

    // URL to fetch yesterdays stock data (TSLA in this case)
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=1min&apikey=DV0ZUWVK94S3TOCY";

    private OnTaskComplete listener; // Callback listener for task completion
    private Context context;

    // Constructor to initialize the task with a listener and context
    public UpdateTask(OnTaskComplete listener, Context context){
        this.listener = listener;
        this.context = context;
    }

    // Background task to fetch data from the provided URL
    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'get' request to URL: " + url);
            System.out.println("Response code: " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Convert the response data (JSON) to a JSONObject
            JSONObject myResponse = new JSONObject(response.toString());
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

    // Method called after the background task is completed; it triggers the callback
    protected void onPostExecute(JSONObject result) {
        listener.onTaskComplete(result);
    }
}
