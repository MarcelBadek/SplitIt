package com.example.splitit.Adapter.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class BillViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView price;
    public TextView paiedBy;
    public TextView members;

    public BillViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        paiedBy = itemView.findViewById(R.id.paied_by);
        members = itemView.findViewById(R.id.members);
    }
}
