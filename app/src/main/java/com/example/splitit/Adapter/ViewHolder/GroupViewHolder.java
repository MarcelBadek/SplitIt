package com.example.splitit.Adapter.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class GroupViewHolder extends RecyclerView.ViewHolder {
    public TextView nameText;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.name);
    }
}
