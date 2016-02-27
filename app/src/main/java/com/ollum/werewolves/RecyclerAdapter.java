package com.ollum.werewolves;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.RecyclerViewHolder>{
    private static final int TYPE_HEAD = 0;
    public static final int TYPE_LIST = 1;
    ArrayList<User> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<User> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_friends_header, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, viewType);
            return recyclerViewHolder;
        } else if (viewType == TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_friends_row, parent, false);
            RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, viewType);
            return recyclerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {

        if (holder.viewType == TYPE_LIST) {
            User user = arrayList.get(position-1);
            holder.username.setText(user.getUsername()); //username_1
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        int viewType;

        public RecyclerViewHolder(View view, int viewType) {
            super(view);
            if (viewType == TYPE_LIST) {
                this.viewType = TYPE_LIST;
                username = (TextView)view.findViewById(R.id.display_friends_row_username);
            } else if (viewType == TYPE_HEAD) {
                this.viewType = TYPE_HEAD;
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_LIST;
        }
    }
}
