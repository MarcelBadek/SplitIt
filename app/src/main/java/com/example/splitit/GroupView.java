package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GroupView extends AppCompatActivity {
    TextView nameTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        String groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");

        nameTV = findViewById(R.id.name);
        nameTV.setText(groupId);
    }
}