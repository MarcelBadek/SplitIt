package com.example.splitit.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.ViewHolder.BillViewHolder;
import com.example.splitit.Model.Bill;
import com.example.splitit.R;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {
    private List<Bill> bills;

    public BillAdapter(List<Bill> bills) {
        this.bills = bills;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        if (bills != null && bills.size() > 0) {
            holder.name.setText(bills.get(position).getName());
            holder.price.setText(String.format("%s$", bills.get(position).getPrice()));
            holder.paiedBy.setText(bills.get(position).getWhoPaied().getEmail());
            StringBuilder builder = new StringBuilder();
            bills.get(position).getMembers().forEach(member -> {
                builder.append(member.getEmail()).append("\n");
            });
            holder.members.setText(builder.toString());
        }
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }
}
