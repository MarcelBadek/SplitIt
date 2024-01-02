package com.example.splitit.Model;

import java.io.Serializable;
import java.util.List;

public class Bill implements Serializable {
    private String id;
    private String name;
    private double price;
    private List<Member> members;
    private Member whoPaied;

    public Bill() {}

    public Bill(String id, String name, double price, List<Member> members, Member whoPaied) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.members = members;
        this.whoPaied = whoPaied;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Member getWhoPaied() {
        return whoPaied;
    }

    public void setWhoPaied(Member whoPaied) {
        this.whoPaied = whoPaied;
    }

}
