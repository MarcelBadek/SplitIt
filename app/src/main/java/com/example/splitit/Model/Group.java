package com.example.splitit.Model;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

    private String id;
    private String name;
    private List<Member> members;
    private List<Bill> bills;

    public Group(String id, String name, List<Member> members, List<Bill> bills) {
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
