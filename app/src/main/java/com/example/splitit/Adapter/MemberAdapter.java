package com.example.splitit.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Adapter.ViewHolder.MemberViewHolder;
import com.example.splitit.Model.Group;
import com.example.splitit.Model.Member;
import com.example.splitit.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<Member> members;
    private HashMap<String, Double> balance;
    private OnClickListener onClickListener;

    public MemberAdapter(List<Member> members, HashMap<String, Double> balance) {
        this.members = members;
        this.balance = balance;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.member_entry,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.emailTV.setText(members.get(position).getEmail());

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.balanceTV.setText(decimalFormat.format(balance.get(members.get(position).getEmail())) + "$");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), members.get(holder.getBindingAdapterPosition()));
                }
            }
        });
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
