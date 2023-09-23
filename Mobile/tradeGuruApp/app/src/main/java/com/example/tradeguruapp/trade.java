package com.example.tradeguruapp;

import java.time.LocalDateTime;

public class trade {
    private String type; // "Buy" or "Short"
    private String companyName;
    private float price;
    private float priceDifference;
    private LocalDateTime timestamp;

    // Constructor
    public trade(String type, String companyName, float priceDifference, LocalDateTime timestamp) {
        this.type = type;
        this.companyName = companyName;
        this.price = price;
        this.priceDifference = priceDifference;
        this.timestamp = timestamp;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public float getPrice() {
        return price;
    }

    public float getPriceDifference() {
        return priceDifference;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
