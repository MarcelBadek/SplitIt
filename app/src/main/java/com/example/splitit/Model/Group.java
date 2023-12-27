package com.example.splitit.Model;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Group {
    private String name;
    private List<FirebaseUser> members;
    private List<Bill> bills;
}
