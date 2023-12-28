package com.example.splitit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.Model.Group;
import com.example.splitit.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class GroupAdapter extends FirestoreRecyclerAdapter<Group, GroupAdapter.GroupViewHolder> {
    FirebaseStorage storage;

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
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
        }
    }
}
