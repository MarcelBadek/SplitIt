package com.example.splitit.Model;

import java.util.List;

public class Group {
    private String name;
    private List<String> members;
    private List<Bill> bills;

    public Group() {}
    public Group(String name, List<String> members, List<Bill> bills) {
        this.name = name;
        this.members = members;
        this.bills = bills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
