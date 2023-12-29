package com.example.splitit;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitit.Model.Group;
import com.example.splitit.Model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class GroupAddMember extends AppCompatActivity {
    TextInputEditText emailTIET;
    Button addMemberBtn;
    TextView backTV;
    String groupId;
    FirebaseFirestore db;
    DocumentReference document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_member);
        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");
        db = FirebaseFirestore.getInstance();
        document = db.collection("groups").document(groupId);

        emailTIET = findViewById(R.id.email);
        addMemberBtn = findViewById(R.id.btn_addMember);
        backTV = findViewById(R.id.back);

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(emailTIET.getText());
                DocumentReference user = db.collection("users").document(email);
                user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Group group = task.getResult().toObject(Group.class);
                                    group.getMembers().add(new Member(email));
                                    document.update("members", group.getMembers()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(getApplicationContext(), GroupView.class);
                                            intent.putExtra("groupId", groupId);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(GroupAddMember.this, "Adding member failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(GroupAddMember.this, "Adding member failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GroupAddMember.this, "Adding member failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupView.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                finish();
            }
        });

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