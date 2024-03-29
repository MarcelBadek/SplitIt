package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.GroupAdapter;
import com.example.splitit.Model.Group;
import com.example.splitit.Model.Member;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    CollectionReference db;
    Button logoutBtn;
    Button createGroupBtn;
    RecyclerView recyclerView;
    GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        db = FirebaseFirestore.getInstance().collection("groups");
        recyclerView = findViewById(R.id.recycler_view);
        Query query = db.whereArrayContains("members", new Member(user.getEmail())).orderBy("name");
        FirestoreRecyclerOptions<Group> options = new FirestoreRecyclerOptions.Builder<Group>()
                .setQuery(query, Group.class)
                .build();
        adapter = new GroupAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new GroupAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Group model) {
                Intent intent = new Intent(getApplicationContext(), GroupView.class);
                intent.putExtra("groupId", model.getId());
                startActivity(intent);
                finish();
            }
        });

        logoutBtn = findViewById(R.id.btn_logout);
        createGroupBtn = findViewById(R.id.btn_createGroup);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupCreate.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}