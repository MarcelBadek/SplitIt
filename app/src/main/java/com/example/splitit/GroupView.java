package com.example.splitit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.MemberAdapter;
import com.example.splitit.Model.Bill;
import com.example.splitit.Model.Group;
import com.example.splitit.Model.Member;
import com.example.splitit.Model.Transaction;
import com.example.splitit.utils.Reckoning;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GroupView extends AppCompatActivity {
    TextView nameTV;
    Button addMemberBtn;
    Button addBillBtn;
    TextView totalCostTV;
    CollectionReference collection;
    String groupId;
    Group group;
    RecyclerView recyclerView;
    MemberAdapter adapter;
    HashMap<String, Double> balance;
    List<Transaction> transactions;
    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        groupId = Objects.requireNonNull(getIntent().getExtras()).getString("groupId");
        decimalFormat = new DecimalFormat("0.00");

        addMemberBtn = findViewById(R.id.btn_addMember);
        addBillBtn = findViewById(R.id.btn_addBill);

        collection = FirebaseFirestore.getInstance().collection("groups");
        collection.document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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

        addBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroupAddBill.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                finish();
            }
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
        nameTV = findViewById(R.id.name);
        totalCostTV = findViewById(R.id.total_cost);
        nameTV.setText(group.getName());

        List<Bill> bills = group.getBills();
        double cost = 0;
        if (bills != null) {
            for (int i = 0; i < bills.size(); i++) {
                cost += bills.get(i).getPrice();
            }
        }
        totalCostTV.setText("Total cost: " + decimalFormat.format(cost) + "$");
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new MemberAdapter(group.getMembers(), balance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new MemberAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Member model) {
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
                        builder.append(decimalFormat.format(transaction.getPrice())).append(" to ").append(transaction.getTo()).append("\n");
                    }
                });

                if (builder.length() == 0) {
                    builder.append("You don't need to send any money! :D");
                }

                textView.setText(builder.toString());

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }
}