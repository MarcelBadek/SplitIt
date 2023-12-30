package com.example.splitit.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.ViewHolder.BillViewHolder;
import com.example.splitit.Model.Member;
import com.example.splitit.R;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {
    private List<Member> members;
    private OnClickListener onClickListener;

    public BillAdapter(List<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_entry,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        if (members != null && members.size() > 0) {
            holder.checkBox.setText(members.get(position).getEmail());

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(holder.getBindingAdapterPosition(), members.get(holder.getBindingAdapterPosition()));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Member model);
    }
}
