package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.MemberAdapter;
import com.example.splitit.Model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class GroupView extends AppCompatActivity {
    TextView nameTV;
    Button addMemberBtn;
    Button addBillBtn;
    CollectionReference collection;
    String groupId;
    Group group;
    RecyclerView recyclerView;
    MemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");

        addMemberBtn = findViewById(R.id.btn_addMember);

        collection = FirebaseFirestore.getInstance().collection("groups");
        collection.document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        group = document.toObject(Group.class);
                        setDisplayInfo();
                        setRecyclerView();
                    } else {
                        Toast.makeText(GroupView.this,"Failed",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(GroupView.this,"Failed",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupAddMember.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setDisplayInfo() {
        nameTV = findViewById(R.id.name);
        nameTV.setText(group.getName());
    }

    private void setRecyclerView() {
        DocumentReference document = FirebaseFirestore.getInstance().collection("groups").document(groupId);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new MemberAdapter(group.getMembers());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }
}