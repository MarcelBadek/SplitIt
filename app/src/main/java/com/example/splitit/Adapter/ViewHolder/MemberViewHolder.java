package com.example.splitit.Adapter.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    public TextView emailTV;
    public TextView balanceTV;
    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);
        emailTV = itemView.findViewById(R.id.email);
        balanceTV = itemView.findViewById(R.id.balance);
    }
}
