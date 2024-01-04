package com.example.splitit.Model;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Member toCompare = (Member) obj;

        return this.getEmail().equals(toCompare.getEmail());
    }
}
