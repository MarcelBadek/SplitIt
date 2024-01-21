package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.MemberAdapter;
import com.example.splitit.Model.Bill;
import com.example.splitit.Model.Group;
import com.example.splitit.Model.Transaction;
import com.example.splitit.utils.Reckoning;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GroupView extends AppCompatActivity {
    TextView nameTV;
    Button addMemberBtn;
    Button addBillBtn;
    Button settleBillsBtn;
    Button showCurrentBillsBtn;
    Button showSettledBillsBtn;
    TextView totalCostTV;
    CollectionReference collection;
    String groupId;
    Group group;
    RecyclerView recyclerView;
    MemberAdapter adapter;
    HashMap<String, Double> balance;
    List<Transaction> transactions;
    DecimalFormat decimalFormat;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        auth = FirebaseAuth.getInstance();
        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");
        decimalFormat = new DecimalFormat("0.00");

        nameTV = findViewById(R.id.name);
        totalCostTV = findViewById(R.id.total_cost);
        recyclerView = findViewById(R.id.recycler_view);

        addMemberBtn = findViewById(R.id.btn_addMember);
        addBillBtn = findViewById(R.id.btn_addBill);
        settleBillsBtn = findViewById(R.id.btn_settleBills);
        showCurrentBillsBtn = findViewById(R.id.btn_showCurrentBills);
        showSettledBillsBtn = findViewById(R.id.btn_showSettledBills);

        collection = FirebaseFirestore.getInstance().collection("groups");
        collection.document(groupId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    group = document.toObject(Group.class);
                    balance = Reckoning.calculateBalance(group);
                    transactions = Reckoning.calculateTransactions(balance);
                    setDisplayInfo();
                    setRecyclerView();
                } else {
                    Toast.makeText(GroupView.this, "Failed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(GroupView.this, "Failed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        addMemberBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GroupAddMember.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
            finish();
        });

        addBillBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), GroupAddBill.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
            finish();
        });

        settleBillsBtn.setOnClickListener(v -> settleBills());

        showCurrentBillsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BillsCurrent.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("group", group);
            startActivity(intent);
            finish();
        });

        showSettledBillsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BillsSettled.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("group", group);
            startActivity(intent);
            finish();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void setDisplayInfo() {
        nameTV.setText(group.getName());

        List<Bill> bills = group.getCurrentBills();
        double cost = 0;
        if (bills != null) {
            for (int i = 0; i < bills.size(); i++) {
                cost += bills.get(i).getPrice();
            }
        }
        totalCostTV.setText("Total cost: " + decimalFormat.format(cost) + "$");
    }

    private void setRecyclerView() {
        adapter = new MemberAdapter(group.getMembers(), balance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener((position, model) -> {
            ConstraintLayout main = findViewById(R.id.main_element);

            View view = View.inflate(GroupView.this, R.layout.activity_pop_up, null);
            Button closeBtn = view.findViewById(R.id.btn_close);
            TextView textView = view.findViewById(R.id.text);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            PopupWindow popupWindow = new PopupWindow(view, width, height, false);
            popupWindow.showAtLocation(main, Gravity.CENTER, 0, 0);
            StringBuilder builder = new StringBuilder();

            transactions.forEach(transaction -> {
                if (Objects.equals(transaction.getFrom(), model.getEmail())) {
                    builder.append(decimalFormat.format(transaction.getPrice())).append("$ to ").append(transaction.getTo()).append("\n");
                }
            });

            if (builder.length() == 0) {
                builder.append("You don't need to send any money! :D");
            }

            textView.setText(builder.toString());

            closeBtn.setOnClickListener(v -> popupWindow.dismiss());
        });
    }

    private void settleBills() {
        FirebaseUser user = auth.getCurrentUser();

        if (!Objects.equals(user.getEmail(), group.getOwner().getEmail())) {
            Toast.makeText(GroupView.this, "Only group owner can settle bills - " +  group.getOwner().getEmail(), Toast.LENGTH_LONG).show();
            return;
        }

        if (group.getCurrentBills() == null) {
            return;
        }
        if (group.getCurrentBills().isEmpty()) {
            return;
        }

        if (group.getSettledBills() == null) {
            group.setSettledBills(new ArrayList<>());
        }

        group.getSettledBills().addAll(group.getCurrentBills());
        group.setCurrentBills(new ArrayList<>());
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("currentBills", group.getCurrentBills());
        updates.put("settledBills", group.getSettledBills());
        collection.document(groupId).update(updates)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(GroupView.this, "Success", Toast.LENGTH_SHORT).show();
                    recreate();
                })
                .addOnFailureListener(e -> Toast.makeText(GroupView.this, "Failure", Toast.LENGTH_SHORT).show());

    }
}