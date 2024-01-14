package com.example.splitit;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.splitit.Adapter.BillAdapter;
import com.example.splitit.Model.Group;

import java.util.ArrayList;
import java.util.Objects;

public class BillsSettled extends AppCompatActivity {
    String groupId;
    Group group;
    BillAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_settled);

        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");
        group = (Group) getIntent().getSerializableExtra("group");

        if (group.getSettledBills() == null) {
            group.setSettledBills(new ArrayList<>());
        }

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new BillAdapter(group.getSettledBills());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), GroupView.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                finish();
            }
        });
    }
}