package com.example.splitit.Model;

import java.io.Serializable;

public class Member implements Serializable {
    private String email;
    public Member() {}

    public Member(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
