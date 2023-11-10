package com.example.kidsland;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<AccountItem> accountItemList;
    private int itemLayout; // Ajout de la variable pour stocker le layout

    public AccountAdapter(List<AccountItem> accountItemList, int itemLayout) {
        this.accountItemList = accountItemList;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountItem accountItem = accountItemList.get(position);
        holder.avatarImageView.setImageResource(accountItem.getAvatarResId());
        holder.childNameTextView.setText(accountItem.getChildName());
    }

    @Override
    public int getItemCount() {
        return accountItemList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView childNameTextView;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            childNameTextView = itemView.findViewById(R.id.childNameTextView);
        }
    }
}
