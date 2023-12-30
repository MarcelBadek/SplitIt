package com.example.splitit.Adapter.ViewHolder;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;

public class BillViewHolder extends RecyclerView.ViewHolder {

    public CheckBox checkBox;
    public BillViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkbox);
    }
}
