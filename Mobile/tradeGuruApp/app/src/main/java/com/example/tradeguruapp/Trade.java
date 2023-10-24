package com.example.tradeguruapp;

import java.time.LocalDateTime;

public class Trade {
    private String type;
    private String companyName;
    private float priceDifference;
    private LocalDateTime timestamp;

    // Constructor
    public Trade(String type, String companyName, float priceDifference, LocalDateTime timestamp) {
        this.type = type;
        this.companyName = companyName;
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

    public float getPriceDifference() {
        return priceDifference;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
