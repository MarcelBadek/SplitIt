package com.example.splitit.Model;

import java.util.List;

public class Bill {
    private String name;
    private List<Member> members;

    public Bill() {}

    public Bill(String name, List<Member> members) {
        this.name = name;
        this.members = members;
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

}
