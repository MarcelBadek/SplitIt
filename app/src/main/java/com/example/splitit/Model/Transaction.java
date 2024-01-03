package com.example.splitit.Model;

import java.io.Serializable;

public class Transaction implements Serializable {
    private final String from;
    private final String to;
    private final Double price;

    public Transaction(String from, String to, Double price) {
        this.from = from;
        this.to = to;
        this.price = price;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getPrice() {
        return price;
    }
}
