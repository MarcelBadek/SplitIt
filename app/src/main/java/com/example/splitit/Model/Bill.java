package com.example.splitit.Model;

import java.util.List;

public class Bill {
    private String name;
    private double price;
    private List<Member> members;

    public Bill() {}

    public Bill(String name, double price, List<Member> members) {
        this.name = name;
        this.price = price;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

}
