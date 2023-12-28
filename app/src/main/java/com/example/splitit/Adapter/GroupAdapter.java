package com.example.splitit.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.splitit.Adapter.ViewHolder.GroupViewHolder;
import com.example.splitit.Model.Group;
import com.example.splitit.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;

public class GroupAdapter extends FirestoreRecyclerAdapter<Group, GroupViewHolder> {
    private FirebaseStorage storage;
    private OnClickListener onClickListener;

    public GroupAdapter(@NonNull FirestoreRecyclerOptions<Group> options, FirebaseStorage storage) {
        super(options);
        this.storage = storage;
    }

    public GroupAdapter(@NonNull FirestoreRecyclerOptions<Group> options) {
        super(options);
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_entry, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull Group model) {
        holder.nameText.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), model);
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Group model);
    }
}
