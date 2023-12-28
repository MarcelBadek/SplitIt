package com.example.splitit.Model;

import java.util.List;

public class Bill {
    private String name;
    private List<String> members;

    public Bill() {}

    public Bill(String name, List<String> members) {
        this.name = name;
        this.members = members;
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

}
