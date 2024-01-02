package com.example.splitit;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.splitit.Model.Group;
import com.example.splitit.Model.Member;
import com.example.splitit.Model.Transaction;
import com.example.splitit.utils.Reckoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MemberReckoning extends AppCompatActivity {
    Group group;
    TextView testTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_reckoning);
        group = (Group) getIntent().getSerializableExtra("group");



        testTV = findViewById(R.id.test);


        List<String> entries = new ArrayList<>();

        HashMap<String, Double> result1 = Reckoning.calculateBalance(group);

        result1.forEach((key, value) -> {
            entries.add(key + " " + value + "\n");
        });
        entries.add("\n");
        entries.add("\n");
        entries.add("\n");

        List<Transaction> reuslt2 = Reckoning.calculateTransactions(result1);
        reuslt2.forEach(transaction -> {
            entries.add("FROM " + transaction.getFrom() + " TO " + transaction.getTo() + " : " + transaction.getPrice() + "\n");
        });

        testTV.setText(entries.toString());






        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), GroupView.class);
                intent.putExtra("groupId", group.getId());
                startActivity(intent);
                finish();
            }
        });
    }
}