package com.example.splitit.Model;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

    private String id;
    private String name;
    private Member owner;
    private List<Member> members;
    private List<Bill> currentBills;
    private List<Bill> settledBills;

    public Group(String id, String name, Member owner, List<Member> members, List<Bill> currentBills) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = members;
        this.currentBills = currentBills;
        settledBills = null;
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

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public List<Bill> getCurrentBills() {
        return currentBills;
    }

    public void setCurrentBills(List<Bill> currentBills) {
        this.currentBills = currentBills;
    }

    public List<Bill> getSettledBills() {
        return settledBills;
    }

    public void setSettledBills(List<Bill> settledBills) {
        this.settledBills = settledBills;
    }
}
