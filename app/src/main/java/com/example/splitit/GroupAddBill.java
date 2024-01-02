package com.example.splitit;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.splitit.Adapter.BillAdapter;
import com.example.splitit.Adapter.MemberAdapter;
import com.example.splitit.Model.Bill;
import com.example.splitit.Model.Group;
import com.example.splitit.Model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GroupAddBill extends AppCompatActivity {
    TextInputEditText nameTIET;
    TextInputEditText priceTIET;
    Button addBillBtn;
    RecyclerView recyclerView;
    BillAdapter adapter;
    String groupId;
    Group group;
    List<Member> selected;
    DocumentReference document;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add_bill);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        selected = new ArrayList<>();
        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");
        document = db.collection("groups").document(groupId);

        nameTIET = findViewById(R.id.name);
        priceTIET = findViewById(R.id.price);
        addBillBtn = findViewById(R.id.btn_addBill);

        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        group = document.toObject(Group.class);
                        setRecyclerView();
                    } else {
                        Toast.makeText(GroupAddBill.this, "Failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(GroupAddBill.this, "Failed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

        addBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = auth.getCurrentUser();
                db.collection("users").document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                Member member = snapshot.toObject(Member.class);

                                String name = String.valueOf(nameTIET.getText());
                                String priceStr = String.valueOf(priceTIET.getText());

                                if (name.length() == 0 || priceStr.length() == 0) {
                                    Toast.makeText(GroupAddBill.this, "Fill all data", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (selected.size() == 0) {
                                    Toast.makeText(GroupAddBill.this, "Select at least 1 member", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                double price = 0;
                                try {
                                    price = Double.parseDouble(priceStr);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    price = Double.parseDouble(df.format(price));
                                } catch (Exception e) {
                                    Toast.makeText(GroupAddBill.this, "Wrong price", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Bill bill = new Bill(UUID.randomUUID().toString(), name, price, selected, member);
                                if (group.getBills() == null) {
                                    group.setBills(new ArrayList<>());
                                }
                                group.getBills().add(bill);

                                document.update("bills", group.getBills()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                        Toast.makeText(GroupAddBill.this, "Adding bill failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });

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

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new BillAdapter(group.getMembers());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new BillAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Member model) {
                if (selected.contains(model)) {
                    selected.remove(model);
                } else {
                    selected.add(model);
                }
            }
        });
    }
}