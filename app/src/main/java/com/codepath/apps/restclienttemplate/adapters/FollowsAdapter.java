package com.codepath.apps.restclienttemplate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FollowsAdapter extends RecyclerView.Adapter<FollowsAdapter.ViewHolder> {

    List<String> follows;

    public FollowsAdapter(List<String> follows) {
        this.follows = follows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String user = follows.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return follows.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String user) {
            tvUser.setText(user);
        }
    }
}
