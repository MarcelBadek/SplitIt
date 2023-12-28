package com.example.splitit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitit.Model.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GroupCreate extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    CollectionReference db;
    TextInputEditText nameEditText;
    TextView backText;
    Button createGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance().collection("groups");
        nameEditText = findViewById(R.id.name);
        backText = findViewById(R.id.back);
        createGroupBtn = findViewById(R.id.btn_createGroup);

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(nameEditText.getText());
                Group newGroup = new Group(name, new ArrayList<>(Collections.singleton(user.getUid())), null);
                try {
                    db.document(UUID.randomUUID().toString()).set(newGroup).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(GroupCreate.this, "Success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GroupCreate.this, "Group creation failed", Toast.LENGTH_SHORT).show();
                                }
                            });;
                } catch (Exception e) {
                    Toast.makeText(GroupCreate.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}