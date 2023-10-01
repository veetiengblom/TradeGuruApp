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

    public CountdownHandler(TradeActivity tradeActivity) {
        this.tradeActivity = tradeActivity;
        buyBtn = tradeActivity.findViewById(R.id.buyBtn);
        shortBtn = tradeActivity.findViewById(R.id.shortBtn);

        countdownText = tradeActivity.findViewById(R.id.countdownTextView);
    }

    public void startCountdown(final Callback callback) {
        buyBtn.setEnabled(false);
        shortBtn.setEnabled(false);
        countdownValue = 5;

        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdownValue >= 0) {
                    countdownText.setText(String.valueOf(countdownValue));
                    countdownValue--;

                    handler.postDelayed(this, 1000);
                } else {
                    buyBtn.setEnabled(true);
                    shortBtn.setEnabled(true);
                    callback.onCountdownFinished();
                }
            }
        };

        handler.post(countdownRunnable);
    }

    public void cancelCountdown() {
        handler.removeCallbacks(countdownRunnable);
        countdownText.setText("");
        buyBtn.setEnabled(true);
        shortBtn.setEnabled(true);
    }

    public interface Callback {
        void onCountdownFinished();
    }


}
