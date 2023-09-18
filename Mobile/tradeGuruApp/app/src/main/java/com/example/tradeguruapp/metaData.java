package com.example.tradeguruapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class metaData {

    @SerializedName("Meta Data")
    private ArrayList<detailData> metaData;

    @SerializedName("Time Series (1min)")
    private ArrayList<minuteData> timeSeries;

    public metaData(ArrayList<detailData> metaData, ArrayList<minuteData> timeSeries) {
        this.metaData = metaData;
        this.timeSeries = timeSeries;
    }

    public ArrayList<detailData> getMetaData() {
        return metaData;
    }

    public void setMetaData(ArrayList<detailData> metaData) {
        this.metaData = metaData;
    }

    public ArrayList<minuteData> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(ArrayList<minuteData> timeSeries) {
        this.timeSeries = timeSeries;
    }
}