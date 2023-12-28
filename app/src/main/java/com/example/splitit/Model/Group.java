package com.example.splitit.Model;

import java.util.List;
import java.util.UUID;

public class Group {

    private String id;
    private String name;
    private List<String> members;
    private List<Bill> bills;

    public Group(String id, String name, List<String> members, List<Bill> bills) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.bills = bills;
    }

    public Group() {
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
