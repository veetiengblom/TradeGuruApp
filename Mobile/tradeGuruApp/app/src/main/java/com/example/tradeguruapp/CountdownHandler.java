package com.example.tradeguruapp;

import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

public class CountdownHandler {

    private TradeActivity tradeActivity;
    private Button buyBtn;
    private Button shortBtn;
    private TextView countdownText;
    private int countdownValue = 5;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;

    // Constructor that initializes the CountdownHandler with TradeActivity and UI elements
    public CountdownHandler(TradeActivity tradeActivity) {
        this.tradeActivity = tradeActivity;
        buyBtn = tradeActivity.findViewById(R.id.buyBtn);
        shortBtn = tradeActivity.findViewById(R.id.shortBtn);
        countdownText = tradeActivity.findViewById(R.id.countdownTextView);
    }

    // Start a countdown with a specified callback when finished
    public void startCountdown(final Callback callback) {
        buyBtn.setEnabled(false);
        shortBtn.setEnabled(false);
        countdownValue = 5;

        // Create a new runnable to update the countdown
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdownValue >= 0) {
                    countdownText.setText(String.valueOf(countdownValue));
                    countdownValue--;

                    // Schedule the runnable to run again after 1 second (1000 milliseconds)
                    handler.postDelayed(this, 1000);
                } else {
                    // Enable the buttons and notify the callback that the countdown is finished
                    buyBtn.setEnabled(true);
                    shortBtn.setEnabled(true);
                    callback.onCountdownFinished();
                }
            }
        };

        // Start the initial countdown
        handler.post(countdownRunnable);
    }

    // Cancel the countdown and reset UI elements
    public void cancelCountdown() {
        handler.removeCallbacks(countdownRunnable);
        countdownText.setText("");
        buyBtn.setEnabled(true);
        shortBtn.setEnabled(true);
    }

    // Callback interface to notify when the countdown is finished
    public interface Callback {
        void onCountdownFinished();
    }
}
