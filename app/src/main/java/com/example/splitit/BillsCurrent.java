package com.example.splitit;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.splitit.Adapter.BillAdapter;
import com.example.splitit.Adapter.MemberAdapter;
import com.example.splitit.Model.Group;

import java.util.Objects;

public class BillsCurrent extends AppCompatActivity {
    String groupId;
    Group group;
    BillAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_current);

        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");
        group = (Group) getIntent().getSerializableExtra("group");

        recyclerView = findViewById(R.id.recycler_view);

        adapter = new BillAdapter(group.getCurrentBills());
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