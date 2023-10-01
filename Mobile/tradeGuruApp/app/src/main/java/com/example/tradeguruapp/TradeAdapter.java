package com.example.tradeguruapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TradeAdapter extends BaseAdapter {

    private Context context;
    private List<Trade> tradeList;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    DecimalFormat moneyFormatter = new DecimalFormat("0.000");


    public TradeAdapter(Context context, List<Trade> tradeList) {
        this.context = context;
        this.tradeList = tradeList;
    }

    @Override
    public int getCount() {
        return tradeList.size();
    }

    @Override
    public Object getItem(int position) {
        return tradeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.trade_item, parent, false);
        }

        Trade tradeItem = tradeList.get(position);

        TextView typeTextView = convertView.findViewById(R.id.typeTextView);
        TextView companyTextView = convertView.findViewById(R.id.companyTextView);
        TextView priceDiffTextView = convertView.findViewById(R.id.priceDiffTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        String timeStamp = tradeItem.getTimestamp().toString();
        timeStamp = timeFormatter.format(LocalDateTime.parse(timeStamp));
        Float m = (tradeItem.getPriceDifference());
        String money = moneyFormatter.format(m);


        // Set the data in the TextViews
        typeTextView.setText(tradeItem.getType());
        companyTextView.setText(tradeItem.getCompanyName());
        priceDiffTextView.setText(money + "$");
        timeTextView.setText("" + timeStamp);

        if (tradeItem.getPriceDifference() > 0) {
            // Green background for positive price difference
            convertView.setBackgroundResource(R.color.greenBackground);
        } else if (tradeItem.getPriceDifference() < 0) {
            // Red background for negative price difference
            convertView.setBackgroundResource(R.color.redBackground);
        } else {
            // Default background color (if needed)
            convertView.setBackgroundResource(android.R.color.transparent);
        }

        return convertView;
    }
}

